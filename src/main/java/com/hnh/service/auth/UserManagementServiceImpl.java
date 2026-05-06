package com.hnh.service.auth;

import com.hnh.entity.authentication.User;
import com.hnh.exception.VerificationException;
import com.hnh.repository.authentication.UserRepository;
import com.hnh.service.email.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional
public class UserManagementServiceImpl implements UserManagementService {

    private UserRepository userRepository;
    private EmailSenderService emailSenderService;

    @Override
    public void approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new VerificationException("Người dùng không tồn tại."));

        if (user.getStatus() != -1) {
            throw new VerificationException("Người dùng không ở trạng thái chờ phê duyệt.");
        }

        user.setStatus(1); // Hoạt động
        userRepository.save(user);

        emailSenderService.sendAccountAccepted(user.getEmail(), Map.of());
    }

    @Override
    public void rejectUser(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new VerificationException("Người dùng không tồn tại."));

        if (user.getStatus() != -1) {
            throw new VerificationException("Người dùng không ở trạng thái chờ phê duyệt.");
        }

        // Gửi email thông báo từ chối trước khi xóa
        emailSenderService.sendAccountRejected(user.getEmail(), Map.of("reason", reason));

        // Xóa tài khoản người dùng
        userRepository.delete(user);
    }
}
