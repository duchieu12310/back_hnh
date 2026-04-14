package com.hnh.service.auth;

import com.hnh.constant.AppConstants;
import com.hnh.dto.authentication.RegistrationRequest;
import com.hnh.dto.authentication.ResetPasswordRequest;
import com.hnh.dto.authentication.UserRequest;
import com.hnh.entity.authentication.Role;
import com.hnh.entity.authentication.User;
import com.hnh.entity.authentication.Verification;
import com.hnh.entity.authentication.VerificationType;
import com.hnh.entity.customer.Customer;
import com.hnh.entity.customer.CustomerGroup;
import com.hnh.entity.customer.CustomerResource;
import com.hnh.entity.customer.CustomerStatus;
import com.hnh.exception.ExpiredTokenException;
import com.hnh.exception.VerificationException;
import com.hnh.mapper.authentication.UserMapper;
import com.hnh.repository.authentication.UserRepository;
import com.hnh.repository.authentication.VerificationRepository;
import com.hnh.repository.customer.CustomerRepository;
import com.hnh.service.email.EmailSenderService;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = VerificationException.class, dontRollbackOn = ExpiredTokenException.class)
public class VerificationServiceImpl implements VerificationService {

    private UserRepository userRepository;
    private VerificationRepository verificationRepository;
    private UserMapper userMapper;
    private EmailSenderService emailSenderService;
    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public Long generateTokenVerify(UserRequest userRequest) {
        // (1) Check if username exists in database
        if (userRepository.existsUserByUsername(userRequest.getUsername())) {
            throw new VerificationException("Username is existing");
        }

        // (2) Check if email existing in database
        if (userRepository.existsUserByEmail(userRequest.getEmail())) {
            throw new VerificationException("Email is existing");
        }

        // (3) Create user entity with status 2 (non-verified) and set role Customer
        User user = userMapper.requestToEntity(userRequest);
        user.setStatus(2); // Non-verified
        user.setRoles(Set.of((Role) new Role().setId(4L)));

        userRepository.save(user);

        // (4) Create new verification entity and set user, token
        Verification verification = new Verification();
        String token = generateVerificationToken();

        verification.setUser(user);
        verification.setToken(token);
        verification.setExpiredAt(Instant.now().plus(5, ChronoUnit.MINUTES));
        verification.setType(VerificationType.REGISTRATION);

        verificationRepository.save(verification);

        // (5) Send email
        Map<String, Object> attributes = Map.of(
                "token", token,
                "link", MessageFormat.format("{0}/signup?userId={1}", AppConstants.FRONTEND_HOST, user.getId()));
        emailSenderService.sendVerificationToken(user.getEmail(), attributes);

        return user.getId();
    }

    @Override
    public void resendRegistrationToken(Long userId) {
        User user = ensureUserIsUnverified(userId);
        Optional<Verification> verifyOpt = verificationRepository.findByUserId(userId);

        if (verifyOpt.isPresent()) {
            Verification verification = verifyOpt.get();
            String token = generateVerificationToken();

            verification.setToken(token);
            verification.setExpiredAt(Instant.now().plus(5, ChronoUnit.MINUTES));

            verificationRepository.save(verification);

            Map<String, Object> attributes = Map.of(
                    "token", token,
                    "link", MessageFormat.format("{0}/signup?userId={1}", AppConstants.FRONTEND_HOST, userId));
            emailSenderService.sendVerificationToken(user.getEmail(), attributes);
        } else {
            throw new VerificationException("Verification record missing for User ID: " + userId + ". Please contact support.");
        }
    }

    @Override
    public void confirmRegistration(RegistrationRequest registration) {
        ensureUserIsUnverified(registration.getUserId());
        Optional<Verification> verifyOpt = verificationRepository.findByUserId(registration.getUserId());

        if (verifyOpt.isPresent()) {
            Verification verification = verifyOpt.get();

            boolean validVerification = verification.getToken().equals(registration.getToken())
                    && verification.getExpiredAt().isAfter(Instant.now())
                    && verification.getType().equals(VerificationType.REGISTRATION);

            if (validVerification) {
                // (1) Set status code and delete row verification
                User user = verification.getUser();
                user.setStatus(1); // Verified
                userRepository.save(user);
                verificationRepository.delete(verification);

                // (2) Create customer entity
                Customer customer = new Customer();
                customer.setUser(user);
                customer.setCustomerGroup((CustomerGroup) new CustomerGroup().setId(1L));
                customer.setCustomerStatus((CustomerStatus) new CustomerStatus().setId(1L));
                customer.setCustomerResource((CustomerResource) new CustomerResource().setId(1L));
                customerRepository.save(customer);
            }

            boolean tokenIsExpired = verification.getToken().equals(registration.getToken())
                    && !verification.getExpiredAt().isAfter(Instant.now())
                    && verification.getType().equals(VerificationType.REGISTRATION);

            if (tokenIsExpired) {
                String token = generateVerificationToken();

                verification.setToken(token);
                verification.setExpiredAt(Instant.now().plus(5, ChronoUnit.MINUTES));

                verificationRepository.save(verification);

                Map<String, Object> attributes = Map.of(
                        "token", token,
                        "link", MessageFormat.format("{0}/signup?userId={1}", AppConstants.FRONTEND_HOST, registration.getUserId()));
                emailSenderService.sendVerificationToken(verification.getUser().getEmail(), attributes);

                throw new ExpiredTokenException("Token is expired, please check your email to get new token!");
            }

            if (!verification.getToken().equals(registration.getToken())) {
                throw new VerificationException("Invalid token");
            }
        } else {
            throw new VerificationException("Verification record missing for the provided User ID.");
        }
    }

    @Override
    public void changeRegistrationEmail(Long userId, String emailUpdate) {
        ensureUserIsUnverified(userId);
        Optional<Verification> verifyOpt = verificationRepository.findByUserId(userId);

        if (verifyOpt.isPresent()) {
            Verification verification = verifyOpt.get();

            User user = verification.getUser();

            // Check if updated email is already in use by another user
            userRepository.findByEmail(emailUpdate).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userId)) {
                    throw new VerificationException("Email is already in use by another account");
                }
            });

            user.setEmail(emailUpdate);
            userRepository.save(user);

            String token = generateVerificationToken();
            verification.setToken(token);
            verification.setExpiredAt(Instant.now().plus(5, ChronoUnit.MINUTES));
            verificationRepository.save(verification);

            Map<String, Object> attributes = Map.of(
                    "token", token,
                    "link", MessageFormat.format("{0}/signup?userId={1}", AppConstants.FRONTEND_HOST, userId));
            emailSenderService.sendVerificationToken(user.getEmail(), attributes);
        } else {
            throw new VerificationException("Verification record missing for User ID: " + userId);
        }
    }

    private User ensureUserIsUnverified(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new VerificationException("User ID " + userId + " does not exist"));

        if (user.getStatus() == 1) {
            throw new VerificationException("Account associated with email " + user.getEmail() + " is already active");
        }

        return user;
    }

    @Override
    public void forgetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email doesn't exist"));

        if (user.getStatus() == 1) {
            String token = RandomString.make(10);
            user.setResetPasswordToken(token);
            userRepository.save(user);

            String link = MessageFormat.format("{0}/change-password?token={1}&email={2}", AppConstants.FRONTEND_HOST, token, email);
            emailSenderService.sendForgetPasswordToken(user.getEmail(), Map.of("link", link));
        } else {
            throw new VerificationException("Account is not activated");
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository
                .findByEmailAndResetPasswordToken(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Email and/or token are invalid"));
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);
    }

    private String generateVerificationToken() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

}

