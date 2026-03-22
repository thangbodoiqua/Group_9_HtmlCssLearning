package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.entity.VerificationCode;
import com.se2.htmlcsslearning.repository.UserRepository;
import com.se2.htmlcsslearning.repository.VerificationRepository;
import com.se2.htmlcsslearning.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private VerificationRepository verifyRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // ================= SIGN UP =================
    @Override
    public void register(Map<String, String> form) {

        String email = form.get("email");
        String password = form.get("password");
        String name = form.get("name");
        String dob = form.get("dob");

        User user = new User();
        user.setEmail(email);
        user.setUserName(name);
        user.setDob(dob);
        user.setUserRole("USER");// k hard code
        user.setRegDate(LocalDateTime.now());

        user.setPassword(passwordEncoder.encode(password));

        userRepo.save(user);
    }

    @Override
    public boolean login(String email, String password) {

        Optional<User> userOpt = userRepo.findByEmail(email);

        if (userOpt.isPresent()) {
            return passwordEncoder.matches(password, userOpt.get().getPassword());
        }
        return false;
    }
    // ================= OTP =================
    @Override
    public String generateOTP(String email) {

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

        Optional<VerificationCode> codeOpt =
                verifyRepo.findByEmailAndOtp(email, otp);

        return codeOpt.isPresent() &&
                codeOpt.get().getExpiredAt().isAfter(LocalDateTime.now());
    }
    // ================= RESET =================
    @Override
    public void resetPassword(String email, String newPassword) {

        User user = userRepo.findByEmail(email).orElseThrow();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepo.save(user);
    }
}
// exception, validate dang ki dang nhap