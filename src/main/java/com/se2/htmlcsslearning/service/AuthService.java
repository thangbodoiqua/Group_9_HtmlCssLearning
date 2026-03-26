package com.se2.htmlcsslearning.service;

import java.util.Map;

public interface AuthService {

    void register(String name, String email, String password, String dob);

    String generateOTP(String email);

    boolean verifyOTP(String email, String otp);

    void resetPassword(String email, String newPassword);
}