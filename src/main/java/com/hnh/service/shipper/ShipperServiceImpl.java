package com.hnh.service.shipper;

import com.hnh.constant.FieldName;
import com.hnh.constant.ResourceName;
import com.hnh.dto.waybill.WaybillResponse;
import com.hnh.entity.authentication.User;
import com.hnh.entity.order.Order;
import com.hnh.entity.waybill.Waybill;
import com.hnh.entity.waybill.WaybillLog;
import com.hnh.exception.ResourceNotFoundException;
import com.hnh.mapper.waybill.WaybillMapper;
import com.hnh.repository.authentication.UserRepository;
import com.hnh.repository.order.OrderRepository;
import com.hnh.repository.waybill.WaybillLogRepository;
import com.hnh.repository.waybill.WaybillRepository;
import com.hnh.service.waybill.WaybillCallbackConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipperServiceImpl implements ShipperService {

    private final WaybillRepository waybillRepository;
    private final WaybillMapper waybillMapper;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final WaybillLogRepository waybillLogRepository;

    @Override
    public List<WaybillResponse> getAvailableWaybills(Double currentLat, Double currentLng) {
        // Status 1: WAITING (Dưới góc độ shipper là 'Đơn hàng sẵn sàng lấy')
        List<Waybill> waybills = waybillRepository.findByStatusAndShipperIsNull(1);

        if (currentLat != null && currentLng != null) {
            return waybills.stream()
                    .sorted(Comparator.comparingDouble(w -> {
                        if (w.getFromWarehouse() != null && w.getFromWarehouse().getAddress() != null &&
                            w.getFromWarehouse().getAddress().getLatitude() != null &&
                            w.getFromWarehouse().getAddress().getLongitude() != null) {
                            return calculateDistance(currentLat, currentLng,
                                    w.getFromWarehouse().getAddress().getLatitude(),
                                    w.getFromWarehouse().getAddress().getLongitude());
                        }
                        // Nếu không có tọa độ, trả về một khoảng cách rất lớn để đẩy xuống cuối
                        return 1_000_000.0; 
                    }))
                    .map(waybillMapper::entityToResponse)
                    .collect(Collectors.toList());
        }

        return waybills.stream()
                .map(waybillMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WaybillResponse confirmPickup(Long waybillId, String username) {
        Waybill waybill = waybillRepository.findById(waybillId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.WAYBILL, FieldName.ID, waybillId));

        if (waybill.getShipper() != null) {
            throw new RuntimeException("This waybill already has a shipper assigned.");
        }

        User shipper = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.USER, FieldName.USERNAME, username));

        waybill.setShipper(shipper);
        waybill.setStatus(WaybillCallbackConstants.SHIPPING); // Status 2: SHIPPING (Đang giao hàng)

        Order order = waybill.getOrder();
        order.setStatus(3); // Status 3: Shipping (Trong logic WaybillServiceImpl cũng dùng 3 cho SHIPPING)

        // Lưu log
        WaybillLog log = new WaybillLog();
        log.setWaybill(waybill);
        log.setPreviousStatus(1);
        log.setCurrentStatus(WaybillCallbackConstants.SHIPPING);
        waybillLogRepository.save(log);

        waybillRepository.save(waybill);
        orderRepository.save(order);

        return waybillMapper.entityToResponse(waybill);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Bán kính trái đất (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
