package com.se2.htmlcsslearning.service;

import java.util.Map;

public interface AuthService {

    void register(Map<String, String> form);

    boolean login(String email, String password);

    String generateOTP(String email);

    boolean verifyOTP(String email, String otp);

    void resetPassword(String email, String newPassword);
}