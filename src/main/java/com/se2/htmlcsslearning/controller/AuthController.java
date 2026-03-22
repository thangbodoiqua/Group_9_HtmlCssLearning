package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.util.Map;
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ================= PAGE =================

    @GetMapping("/signin")
    public String signin() {
        return "auth/signin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "auth/signup";
    }

    @GetMapping("/forget")
    public String forget() {
        return "auth/forgot-password";
    }

    @GetMapping("/forget/otp")
    public String otpPage() {
        return "auth/forgot-password-otp";
    }

    @GetMapping("/forget/reset")
    public String resetPage() {
        return "auth/forgot-password-reset";
    }

    // ================= SIGN UP =================

    @PostMapping("/signup")
    public String signupPost(@RequestParam Map<String, String> form) {
// validate data user up
        authService.register(form);

        return "redirect:/auth/signin";
    }

    // ================= SIGN IN =================

    @PostMapping("/signin")
    public String signinPost(@RequestParam Map<String, String> form) {

        String email = form.get("email");
        String password = form.get("password");

        if (authService.login(email, password)) {
            return "redirect:/home";
        }

        return "auth/signin";
    }

    // ================= FORGOT =================

    @PostMapping("/forget")
    public String sendOtp(@RequestParam Map<String, String> form,
                          Model model) {

        String email = form.get("email");

        String otp = authService.generateOTP(email);

        System.out.println("OTP: " + otp);

        model.addAttribute("email", email);

        return "auth/forgot-password-otp";
    }

    // ================= VERIFY OTP =================

    @PostMapping("/forget/reset")
    public String verifyOtpAndReset(@RequestParam Map<String, String> form,
                                    Model model) {

        String email = form.get("email");

        // ⚠️ GHÉP OTP từ 6 ô (do bạn không sửa HTML)
        String otp =
                form.getOrDefault("otp1","") +
                        form.getOrDefault("otp2","") +
                        form.getOrDefault("otp3","") +
                        form.getOrDefault("otp4","") +
                        form.getOrDefault("otp5","") +
                        form.getOrDefault("otp6","");

        // nếu có password thì là bước reset
        String password = form.get("password");

        // STEP 1: verify OTP
        if (password == null) {

            if (authService.verifyOTP(email, otp)) {
                model.addAttribute("email", email);
                return "auth/forgot-password-reset";
            }

            return "auth/forgot-password-otp";
        }

        // STEP 2: reset password
        authService.resetPassword(email, password);

        return "auth/reset-password-success";
    }
}