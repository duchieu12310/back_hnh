package com.hnh.service.openai;

import com.hnh.entity.product.Category;
import com.hnh.entity.product.Product;
import com.hnh.repository.product.CategoryRepository;
import com.hnh.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String chatWithGpt(String userMessage) {
        return chatWithGpt(userMessage, null);
    }

    public String chatWithGpt(String userMessage, String customSystemPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String systemPrompt = customSystemPrompt;
        
        if (systemPrompt == null) {
            // 1. Cố gắng tìm kiếm thông minh hơn (Dành cho chat tìm sách)
            List<Product> products = null;
            List<Category> allCategories = categoryRepository.findAll();
            Category foundCategory = null;
            for (Category cat : allCategories) {
                if (userMessage.toLowerCase().contains(cat.getName().toLowerCase())) {
                    foundCategory = cat;
                    break;
                }
            }

            if (foundCategory != null) {
                String filter = "categories.id==" + foundCategory.getId();
                products = productRepository.findByParams(filter, null, null, true, false, false, PageRequest.of(0, 20)).getContent();
            } else {
                products = productRepository.findByParams(null, null, userMessage, true, false, false, PageRequest.of(0, 20)).getContent();
            }
            
            if (products == null || products.isEmpty()) {
                products = productRepository.findAll(PageRequest.of(0, 20)).getContent();
            }

            String bookList = products.stream()
                    .map(p -> {
                        String imageUrl = "https://via.placeholder.com/150";
                        if (p.getImages() != null && !p.getImages().isEmpty()) {
                            imageUrl = "http://localhost:8085/images/" + p.getImages().get(0).getPath();
                        }
                        String categories = p.getCategories().stream().map(c -> c.getName()).collect(Collectors.joining(", "));
                        String author = p.getBrand() != null ? p.getBrand().getName() : "Đang cập nhật";
                        String priceInfo = "Liên hệ";
                        if (p.getVariants() != null && !p.getVariants().isEmpty()) {
                            double minPrice = p.getVariants().stream().mapToDouble(v -> v.getPrice()).min().orElse(0);
                            priceInfo = String.format("%,.0f VNĐ", minPrice);
                        }
                        return String.format("- Tên: %s\n  Slug: %s\n  Ảnh: %s\n  Tác giả: %s\n  Thể loại: %s\n  Giá: %s",
                                p.getName(), p.getSlug(), imageUrl, author, categories, priceInfo);
                    })
                    .collect(Collectors.joining("\n\n"));

            systemPrompt = "Bạn là trợ lý nhà sách. Dưới đây là danh sách sách hiện có:\n\n"
                    + bookList + "\n\n"
                    + "Nhiệm vụ của bạn:\n"
                    + "1. Gợi ý sách cực kỳ ngắn gọn dưới dạng Thẻ sách Markdown (chỉ hiện tối đa 3 thông tin quan trọng nhất):\n"
                    + "   ![Tên sách](URL_Ảnh)\n"
                    + "   **[Tên sách](http://localhost:3000/product/{slug})**\n"
                    + "   - Giá: {Giá} (BẮT BUỘC)\n"
                    + "   - Tác giả: {Tác giả}\n"
                    + "   - Thể loại: {Thể loại}\n"
                    + "2. Trả lời thẳng vào vấn đề, không viết lời dẫn dài dòng. Nếu không có sách phù hợp, báo là hiện chưa có.";
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", systemPrompt),
            Map.of("role", "user", "content", userMessage)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        try {
            Map<String, Object> response = restTemplate.postForObject(apiUrl, entity, Map.class);
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "Xin lỗi, hiện tại tôi không thể trả lời.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Có lỗi xảy ra khi kết nối với AI.";
        }
    }
}
