package com.hnh.controller.client;

import com.hnh.constant.AppConstants;
import com.hnh.service.order.OrderService;
import com.hnh.utils.VNpayService;
import com.hnh.dto.VNpayResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/client-api/vnpay")
@RequiredArgsConstructor
public class ClientVNPay {

    private final VNpayService vnpayService;
    private final OrderService orderService;
    @GetMapping("/return")
    public RedirectView returnFromVNPay(HttpServletRequest request) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            var result = vnpayService.handleResult(paramMap);
            System.out.println("result = " + result);
            
            // Only process order if order ID is valid
            if (result.getOrderId() != null && !result.getOrderId().isEmpty()) {
                orderService.captureTransactionVNPay(result.getOrderId(), result.isSuccess());
            }
            
            RedirectView redirectView = new RedirectView();
            if (result.isSuccess()) {
                System.out.println("Chuyen huong den trang thanh cong");
                redirectView.setUrl(AppConstants.FRONTEND_HOST + "/payment/success");
            } else {
                System.out.println("Chuyen huong den trang that bai");
                redirectView.setUrl(AppConstants.FRONTEND_HOST + "/payment/cancel");
            }

            return redirectView;
        } catch (Exception e) {
            System.out.println("Error processing VNPay return: " + e.getMessage());
            e.printStackTrace();
            // Redirect to cancel page on error
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(AppConstants.FRONTEND_HOST + "/payment/cancel");
            return redirectView;
        }
    }
}

