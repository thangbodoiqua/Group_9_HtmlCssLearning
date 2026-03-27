package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.domain.UserRoleConstants;
import com.se2.htmlcsslearning.dto.request.SignUpRequest;
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
        user.setDob(request.getDob()); // now LocalDate → LocalDate, no parsing needed
        user.setUserRole(UserRoleConstants.USER.name());
        user.setRegDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    // ================= OTP =================
    @Override
    public String generateOTP(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not registered"));

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        VerificationCode code = new VerificationCode();
        code.setUser(user);
        code.setEmail(email);
        code.setOtp(otp);
        code.setCreatedAt(LocalDateTime.now());
        code.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        verificationRepository.save(code);
        sendEmail(email, otp);

        return otp;
    }

    private void sendEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Markuply");
        message.setTo(toEmail);
        message.setSubject("OTP to reset password - Markuply");
        message.setText("Hello,\n\nYour OTP is: " + otp +
        "\nThis OTP is valid for 5 minutes. Please do not share this OTP with anyone.");
        mailSender.send(message);
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (otp == null || otp.length() != 6) {
            throw new IllegalArgumentException("Invalid OTP format");
        }
        Optional<VerificationCode> codeOpt = verificationRepository.findByEmailAndOtp(email, otp);

        if (codeOpt.isEmpty()) return false;

        VerificationCode code = codeOpt.get();
        return !code.getExpiredAt().isBefore(LocalDateTime.now());
    }

    // ================= RESET =================
    @Override
    public void resetPassword(String email, String newPassword) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        User user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
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