package com.hnh.service.waybill;

import com.hnh.dto.waybill.WaybillRequest;
import com.hnh.entity.order.Order;
import com.hnh.entity.waybill.RequiredNote;
import com.hnh.repository.order.OrderRepository;
import com.hnh.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class AutoWaybillService {

    private final OrderRepository orderRepository;
    private final WaybillService waybillService;

    // @Lazy để tránh circular dependency: OrderService -> AutoWaybillService -> OrderService
    @Lazy
    @Autowired
    private OrderService orderService;

    private boolean autoWaybillEnabled = false;

    // Từ khóa lỗi hết hàng — chỉ hủy đơn khi gặp các lỗi này
    private static final List<String> STOCK_ERROR_KEYWORDS = List.of(
        "Không tìm thấy kho nào",
        "không đủ số lượng",
        "out of stock",
        "insufficient"
    );

    public AutoWaybillService(OrderRepository orderRepository, WaybillService waybillService) {
        this.orderRepository = orderRepository;
        this.waybillService = waybillService;
    }

    public boolean isAutoWaybillEnabled() {
        return autoWaybillEnabled;
    }

    public void setAutoWaybillEnabled(boolean autoWaybillEnabled) {
        this.autoWaybillEnabled = autoWaybillEnabled;
        log.info("Auto Waybill feature is now: {}", autoWaybillEnabled ? "ENABLED" : "DISABLED");
    }

    /**
     * Xử lý một đơn hàng cụ thể ngay lập tức (real-time khi đặt hàng thành công)
     */
    public void processOrderImmediately(Order order) {
        if (!autoWaybillEnabled) {
            return;
        }
        log.info("AutoWaybillService [REALTIME]: Processing order immediately: {}", order.getCode());
        processOrder(order);
    }

    /**
     * Scheduled job: chạy mỗi 60 giây để xử lý các đơn còn tồn đọng
     */
    @Scheduled(fixedDelay = 60000)
    public void processPendingOrders() {
        if (!autoWaybillEnabled) {
            return;
        }

        List<Order> pendingOrders = orderRepository.findByStatusOrderByCreatedAtAsc(1);
        if (pendingOrders.isEmpty()) {
            return;
        }

        log.info("AutoWaybillService [SCHEDULED]: Found {} pending orders.", pendingOrders.size());
        for (Order order : pendingOrders) {
            processOrder(order);
        }
    }

    /**
     * Logic xử lý chung cho một đơn hàng
     */
    private void processOrder(Order order) {
        try {
            WaybillRequest waybillRequest = new WaybillRequest();
            waybillRequest.setOrderId(order.getId());
            waybillRequest.setShippingDate(Instant.now().plus(1, java.time.temporal.ChronoUnit.HOURS));
            waybillRequest.setWeight(500);
            waybillRequest.setLength(10);
            waybillRequest.setWidth(10);
            waybillRequest.setHeight(10);
            waybillRequest.setNote("Auto generated waybill");
            waybillRequest.setGhnRequiredNote(RequiredNote.CHOXEMHANGKHONGTHU);

            log.info("AutoWaybillService: Creating waybill for order: {}", order.getCode());
            waybillService.save(waybillRequest);
            log.info("AutoWaybillService: Successfully created waybill for order: {}", order.getCode());

        } catch (Exception e) {
            String errorMsg = e.getMessage() != null ? e.getMessage() : "";
            boolean isStockError = STOCK_ERROR_KEYWORDS.stream()
                    .anyMatch(keyword -> errorMsg.toLowerCase().contains(keyword.toLowerCase()));

            if (isStockError) {
                // Hết tồn kho → hủy đơn
                log.error("AutoWaybillService: Order {} out of stock: {}. Cancelling.", order.getCode(), errorMsg);
                try {
                    orderService.cancelOrder(order.getCode());
                    log.info("AutoWaybillService: Cancelled order {} due to stock issue.", order.getCode());
                } catch (Exception ex) {
                    log.error("AutoWaybillService: Failed to cancel order {}: {}", order.getCode(), ex.getMessage());
                }
            } else {
                // Lỗi khác (GHN API, địa chỉ...) → Giữ nguyên đơn, xử lý thủ công
                log.warn("AutoWaybillService: Skipping order {} (non-stock error): {}", order.getCode(), errorMsg);
            }
        }
    }
}
