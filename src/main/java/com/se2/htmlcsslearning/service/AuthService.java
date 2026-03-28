package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.dto.request.SignUpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public interface AuthService extends UserDetailsService {

    void register(SignUpRequest request);

    String generateOTP(String email);

    boolean verifyOTP(String email, String otp);

    void resetPassword(String email, String newPassword);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}