package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.domain.UserRoleConstants;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.entity.VerificationCode;
import com.se2.htmlcsslearning.repository.UserRepository;
import com.se2.htmlcsslearning.repository.VerificationRepository;
import com.se2.htmlcsslearning.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VerificationRepository verifyRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // ================= SIGN UP =================
    @Override
    public void register(String name, String email, String password, String dob) {

        // ===== VALIDATION =====
        LocalDate birthDate = LocalDate.parse(dob);
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The birth date cannot be later than the current date.");
        }
        if (userRepo.findByEmail(email).isPresent()) {
            throw new IllegalStateException("This email address has already been used.");
        }
        User user = new User();
        user.setEmail(email);
        user.setUserName(name);
        user.setDob(dob);
        user.setUserRole(UserRoleConstants.USER.name());
        user.setRegDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }


    // ================= OTP =================
    @Override
    public String generateOTP(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not registered"));
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        VerificationCode code = new VerificationCode();
        code.setUser(user);
        code.setEmail(email);
        code.setOtp(otp);
        code.setCreatedAt(LocalDateTime.now());
        code.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        verifyRepo.save(code);
        sendEmail(email, otp);

        return otp;
    }
    @Autowired
    private org.springframework.mail.javamail.JavaMailSender mailSender;
    private void sendEmail(String toEmail, String otp) {
        org.springframework.mail.SimpleMailMessage message = new org.springframework.mail.SimpleMailMessage();
        message.setFrom("Markuply <tuanh2004@gmail.com>");
        message.setTo(toEmail);
        message.setSubject("Mã xác thực khôi phục mật khẩu - Markuply");
        message.setText("Xin chào,\n\nMã OTP của bạn là: " + otp +
                "\nMã này có hiệu lực trong 5 phút. Vui lòng không chia sẻ mã này cho bất kỳ ai.");
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
        Optional<VerificationCode> codeOpt =
                verifyRepo.findByEmailAndOtp(email, otp);

        if (codeOpt.isEmpty()) {
            return false;
        }

        VerificationCode code = codeOpt.get();

        if (code.getExpiredAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
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

        User user = userRepo.findByEmail(email).orElseThrow();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepo.save(user);
    }
    // ================= Sign In =================
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UsernameNotFoundException("Email is required");
        }
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getUserRole())
                .build();
    }

}