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
import java.time.LocalDateTime;
import java.util.Map;
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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if (dob == null || dob.trim().isEmpty()) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        if (userRepo.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
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

    @Override
    public boolean login(String email, String password) {

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return true;
    }
    // ================= OTP =================
    @Override
    public String generateOTP(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not registered"));
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        VerificationCode code = new VerificationCode();
        code.setEmail(email);
        code.setOtp(otp);
        code.setCreatedAt(LocalDateTime.now());
        code.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        verifyRepo.save(code);

        return otp;
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

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getUserRole())
                .build();
    }
}