package com.hnh.service.geocode;

import com.hnh.dto.geocode.GeocodeResponse;
import com.hnh.entity.address.District;
import com.hnh.entity.address.Province;
import com.hnh.entity.address.Ward;
import com.hnh.repository.address.DistrictRepository;
import com.hnh.repository.address.ProvinceRepository;
import com.hnh.repository.address.WardRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeocodeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<String, GeocodeResponse> cache = new ConcurrentHashMap<>();

    private final ProvinceRepository provinceRepo;
    private final DistrictRepository districtRepo;
    private final WardRepository wardRepo;

    public GeocodeService(ProvinceRepository provinceRepo,
                          DistrictRepository districtRepo,
                          WardRepository wardRepo) {
        this.provinceRepo = provinceRepo;
        this.districtRepo = districtRepo;
        this.wardRepo = wardRepo;
    }

    public GeocodeResponse getCoordinates(Long provinceId, Long districtId, Long wardId) {
        // Cache theo cả 3 cấp: Tỉnh + Huyện + Xã
        String key = provinceId + "_" + districtId + "_" + wardId;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        Province p = provinceRepo.findById(provinceId).orElse(null);
        District d = districtRepo.findById(districtId).orElse(null);
        Ward w = wardId != null ? wardRepo.findById(wardId).orElse(null) : null;

        if (p == null || d == null) {
            return null;
        }

        GeocodeResponse result = null;

        // Thử lần 1: Xã + Huyện + Tỉnh + Vietnam (chính xác nhất)
        if (w != null) {
            result = fetchFromMap(String.format("%s, %s, %s, Vietnam", w.getName(), d.getName(), p.getName()));
        }

        // Thử lần 2: Xã + Huyện + Tỉnh (bỏ tiền tố)
        if (result == null && w != null) {
            result = fetchFromMap(String.format("%s, %s, %s", cleanName(w.getName()), cleanName(d.getName()), cleanName(p.getName())));
        }

        // Thử lần 3: Huyện + Tỉnh + Vietnam
        if (result == null) {
            result = fetchFromMap(String.format("%s, %s, Vietnam", d.getName(), p.getName()));
        }

        // Thử lần 4: Huyện + Tỉnh (bỏ tiền tố)
        if (result == null) {
            result = fetchFromMap(String.format("%s, %s", cleanName(d.getName()), cleanName(p.getName())));
        }

        // Thử lần 5: Chỉ tên Huyện
        if (result == null) {
            result = fetchFromMap(cleanName(d.getName()));
        }

        // Thử dự phòng: Tiếng Việt KHÔNG DẤU
        if (result == null) {
            String noAccent = w != null
                ? removeAccents(String.format("%s, %s, %s", w.getName(), d.getName(), p.getName()))
                : removeAccents(String.format("%s, %s", d.getName(), p.getName()));
            result = fetchFromMap(noAccent);
        }

        if (result != null) {
            cache.put(key, result);
        }

        return result;
    }

    private GeocodeResponse fetchFromMap(String query) {
        try {
            // Đợi 2s giữa mỗi request theo chính sách Nominatim
            waitALittle();

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8).replace("+", "%20");
            // Thêm email theo khuyến nghị của Nominatim để được ưu tiên
            String url = "https://nominatim.openstreetmap.org/search?format=json&limit=1&email=hnh.store@gmail.com&q=" + encodedQuery;

            System.out.println("Geocoding Request: " + url);

            HttpHeaders headers = new HttpHeaders();
            // Nominatim YÊU CẦU User-Agent riêng của ứng dụng, KHÔNG ĐƯỢC giả lập trình duyệt!
            headers.set("User-Agent", "HNH-Store/1.0 (hnh.store@gmail.com)");
            headers.set("Accept", "application/json");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            // QUAN TRỌNG: Dùng URI thay vì String để tránh RestTemplate encode URL lần 2!
            URI uri = new URI(url);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                System.out.println("Geocoding Error: Status " + response.getStatusCode());
                return null;
            }

            // Log nội dung thực tế API trả về để debug
            String body = response.getBody();
            System.out.println("Geocoding Raw Response: " + (body.length() > 200 ? body.substring(0, 200) + "..." : body));

            List<Map<String, Object>> results = objectMapper.readValue(body, new TypeReference<List<Map<String, Object>>>() {});
            
            if (results == null || results.isEmpty()) {
                System.out.println("Geocoding Result: EMPTY for [" + query + "]");
                return null;
            }

            Map<String, Object> firstResult = results.get(0);
            System.out.println("Geocoding Result: FOUND -> " + firstResult.get("display_name"));
            
            Double lat = Double.parseDouble(firstResult.get("lat").toString());
            Double lon = Double.parseDouble(firstResult.get("lon").toString());

            System.out.println(">>> Parsed lat=" + lat + ", lon=" + lon);

            return new GeocodeResponse(lat, lon);
        } catch (Exception e) {
            System.err.println("Geocode API error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    private void waitALittle() {
        try {
            Thread.sleep(500); // Hạ xuống 0.5s (Cảnh báo: Có thể bị Nominatim chặn nếu gọi quá nhanh)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String cleanName(String name) {
        if (name == null) return "";
        return name.replace("Tỉnh ", "")
                   .replace("Thành phố ", "")
                   .replace("Quận ", "")
                   .replace("Huyện ", "")
                   .replace("Thị xã ", "")
                   .replace("Phường ", "")
                   .replace("Xã ", "")
                   .trim();
    }

    private String removeAccents(String str) {
        if (str == null) return null;
        String nfdNormalizedString = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
}
