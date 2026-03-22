package com.thatannhien.service.email;

import java.util.Map;

public interface EmailSenderService {

    void sendVerificationToken(String toEmail, Map<String, Object> attributes);

    void sendForgetPasswordToken(String toEmail, Map<String, Object> attributes);

    // TODO: TẠM THỜI COMMENT - ĐĂNG KÝ NHẬN KM
    // void sendNewsletterConfirmation(String toEmail, Map<String, Object> attributes);

}
