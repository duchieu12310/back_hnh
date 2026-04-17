package com.hnh.service.auth;

import com.hnh.dto.authentication.RegistrationRequest;
import com.hnh.dto.authentication.ResetPasswordRequest;
import com.hnh.dto.authentication.UserRequest;

public interface VerificationService {

    Long generateTokenVerify(UserRequest userRequest);

    void resendRegistrationToken(Long userId);
    void resendRegistrationToken(String email);

    void confirmRegistration(RegistrationRequest registration);

    void changeRegistrationEmail(Long userId, String emailUpdate);

    void forgetPassword(String email);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

}

