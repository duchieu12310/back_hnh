package com.hnh.service.waybill;

import com.hnh.entity.general.Notification;
import com.hnh.entity.general.NotificationType;
import com.hnh.entity.order.Order;
import com.hnh.entity.waybill.Waybill;
import com.hnh.entity.waybill.WaybillLog;
import com.hnh.mapper.general.NotificationMapper;
import com.hnh.repository.general.NotificationRepository;
import com.hnh.repository.order.OrderRepository;
import com.hnh.repository.waybill.WaybillLogRepository;
import com.hnh.repository.waybill.WaybillRepository;
import com.hnh.service.general.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Real-time delivery completion:
 * Khi tạo vận đơn → schedule task tại đúng expectedDeliveryTime → order.status = 4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCompletionService {

    private final WaybillRepository waybillRepository;
    private final WaybillLogRepository waybillLogRepository;
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final TaskScheduler taskScheduler;

    /**
     * Gọi ngay sau khi tạo vận đơn thành công để lên lịch tự hoàn thành
     */
    public void scheduleDeliveryCompletion(Waybill waybill) {
        Instant deliveryTime = waybill.getExpectedDeliveryTime();
        if (deliveryTime == null) return;

        // Nếu đã qua rồi → thực hiện ngay
        if (deliveryTime.isBefore(Instant.now())) {
            log.info("DeliveryCompletionService: expectedDeliveryTime already past for waybill {}, completing now.", waybill.getCode());
            completeDelivery(waybill.getId());
            return;
        }

        log.info("DeliveryCompletionService: Scheduled delivery completion for waybill {} at {}", waybill.getCode(), deliveryTime);
        taskScheduler.schedule(() -> completeDelivery(waybill.getId()), deliveryTime);
    }

    /**
     * Khi server khởi động lại: re-schedule các đơn chưa hoàn thành
     */
    @EventListener(ApplicationReadyEvent.class)
    public void rescheduleOnStartup() {
        List<Waybill> pendingWaybills = waybillRepository
                .findByStatusInAndExpectedDeliveryTimeBefore(List.of(1, 2), Instant.now().plusSeconds(86400 * 365));

        log.info("DeliveryCompletionService [STARTUP]: Re-scheduling {} pending waybills.", pendingWaybills.size());
        for (Waybill waybill : pendingWaybills) {
            scheduleDeliveryCompletion(waybill);
        }
    }

    @Transactional
    public void completeDelivery(Long waybillId) {
        Waybill waybill = waybillRepository.findById(waybillId).orElse(null);
        if (waybill == null) return;

        Order order = waybill.getOrder();

        // Chỉ xử lý nếu đơn đang ở trạng thái chờ hoặc đang xử lý
        if (order.getStatus() == 4 || order.getStatus() == 5) {
            return; // Đã hoàn thành hoặc đã hủy → bỏ qua
        }

        // Cập nhật waybill → status 3 (Giao thành công)
        WaybillLog waybillLog = new WaybillLog();
        waybillLog.setWaybill(waybill);
        waybillLog.setPreviousStatus(waybill.getStatus());
        waybillLog.setCurrentStatus(3);
        waybillLogRepository.save(waybillLog);

        waybill.setStatus(3);
        waybillRepository.save(waybill);

        // Cập nhật order → status 4 (Đã giao hàng)
        order.setStatus(4);
        order.setPaymentStatus(2); // COD → coi như đã thu tiền
        orderRepository.save(order);

        // Gửi thông báo cho khách
        Notification notification = new Notification()
                .setUser(order.getUser())
                .setType(NotificationType.ORDER)
                .setMessage(String.format("Đơn hàng %s của bạn đã được giao thành công!", order.getCode()))
                .setAnchor("/order/detail/" + order.getCode())
                .setStatus(1);

        notificationRepository.save(notification);
        notificationService.pushNotification(
                order.getUser().getUsername(),
                notificationMapper.entityToResponse(notification)
        );

        log.info("DeliveryCompletionService: Order {} marked as DELIVERED (status=4).", order.getCode());
    }
}
