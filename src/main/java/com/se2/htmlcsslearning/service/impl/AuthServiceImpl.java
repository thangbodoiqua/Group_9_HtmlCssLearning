package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.dto.request.*;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.entity.VerificationCode;
import com.se2.htmlcsslearning.exception.EmailAlreadyExistsException;
import com.se2.htmlcsslearning.repository.UserRepository;
import com.se2.htmlcsslearning.repository.VerificationRepository;
import com.se2.htmlcsslearning.config.CustomUserDetails;
import com.se2.htmlcsslearning.service.AuthService;
import com.se2.htmlcsslearning.service.MailService;
import com.se2.htmlcsslearning.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void register(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUserName(request.getName());
        user.setDob(request.getDob());
        user.setUserRole("USER");
        user.setRegDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void sendOtp(ForgotPasswordRequest request) throws Exception{
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        VerificationCode existedCode = verificationRepository.findByEmail(request.getEmail());

        if(existedCode != null) {
            verificationRepository.delete(existedCode);
        }

        String otp = EmailUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setEmail(request.getEmail());
        verificationCode.setOtp(otp);
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        verificationRepository.save(verificationCode);
        mailService.sendEmail(new MailRequest(request.getEmail(), "OTP Reset Password", otp));
    }



    @Override
    public boolean verifyOTP(VerifyOtpRequest request) {
        VerificationCode verificationCode = verificationRepository.findByEmailAndOtp(
                request.getEmail(),
                request.getOtp()
        );

        if (verificationCode == null) return false;

        if (verificationCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            verificationRepository.delete(verificationCode);
            return false;
        }
        return true;
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UsernameNotFoundException("Email is required");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}