package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.dto.request.ForgotPasswordRequest;
import com.se2.htmlcsslearning.dto.request.ResetPasswordRequest;
import com.se2.htmlcsslearning.dto.request.SignUpRequest;
import com.se2.htmlcsslearning.dto.request.VerifyOtpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public interface AuthService extends UserDetailsService {

    void register(SignUpRequest request);

    String generateOTP(ForgotPasswordRequest request);

    boolean verifyOTP(VerifyOtpRequest request);

    void resetPassword(ResetPasswordRequest request);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}