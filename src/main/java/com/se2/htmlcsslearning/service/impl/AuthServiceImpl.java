package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.domain.UserRoleConstants;
import com.se2.htmlcsslearning.dto.request.*;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.entity.VerificationCode;
import com.se2.htmlcsslearning.exception.EmailAlreadyExistsException;
import com.se2.htmlcsslearning.repository.UserRepository;
import com.se2.htmlcsslearning.repository.VerificationRepository;
import com.se2.htmlcsslearning.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    // ================= SIGN UP =================
    @Override
    public void register(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUserName(request.getName());
        user.setDob(request.getDob());
        user.setUserRole(UserRoleConstants.USER.name());
        user.setRegDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    // ================= OTP =================
    @Override
    public String generateOTP(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not registered"));
       String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        VerificationCode code = VerificationCode.builder()
                .user(user)
                .email(request.getEmail())
                .otp(otp)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .build();

        verificationRepository.save(code);
        sendEmail(new MailRequest(request.getEmail(), "OTP Reset Password", otp));

        return otp;
    }

    private void sendEmail(MailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Markuply");
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText("Hello,\n\nYour OTP is: " + request.getOtp() +
        "\nThis OTP is valid for 5 minutes. Please do not share this OTP with anyone.");
        mailSender.send(message);
    }

    @Override
    public boolean verifyOTP(VerifyOtpRequest request) {

        Optional<VerificationCode> codeOpt = verificationRepository.findByEmailAndOtp(request.getEmail(), request.getOtp());

        if (codeOpt.isEmpty()) return false;

        VerificationCode code = codeOpt.get();
        return !code.getExpiredAt().isBefore(LocalDateTime.now());
    }

    // ================= RESET =================
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // ================= SIGN IN =================
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UsernameNotFoundException("Email is required");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new com.se2.htmlcsslearning.security.CustomUserDetails(user);
    }
}