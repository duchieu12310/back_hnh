package com.thatannhien.service.auth;

import com.thatannhien.dto.authentication.RegistrationRequest;
import com.thatannhien.dto.authentication.ResetPasswordRequest;
import com.thatannhien.dto.authentication.UserRequest;

public interface VerificationService {

    Long generateTokenVerify(UserRequest userRequest);

    void resendRegistrationToken(Long userId);

    void confirmRegistration(RegistrationRequest registration);

    void changeRegistrationEmail(Long userId, String emailUpdate);

    void forgetPassword(String email);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

}
