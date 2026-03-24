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
    @GetMapping("/reset-password-success")
    public String successPage() {
        return "auth/reset-password-success";
    }

    // ================= SIGN UP =================

    @PostMapping("/signup")
    public String signupPost(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String dob,
            Model model
    ) {

        if (name == null || name.trim().isEmpty()) {
            model.addAttribute("error", "Name is required");
            return "auth/signup";
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("error", "Invalid email");
            return "auth/signup";
        }

        if (password == null || password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            return "auth/signup";
        }

        if (dob == null || dob.trim().isEmpty()) {
            model.addAttribute("error", "Date of birth is required");
            return "auth/signup";
        }
        authService.register(name, email, password, dob);

        return "redirect:/auth/signin";
    }
    // ================= SIGN IN =================

    @PostMapping("/signin")
    public String signinPost(
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {

        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "Email is required");
            return "auth/signin";
        }

        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "Password is required");
            return "auth/signin";
        }

        if (authService.login(email, password)) {
            return "redirect:/";
        }

        model.addAttribute("error", "Invalid email or password");
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
    public String verifyOtpAndReset(
            @RequestParam String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String otp1,
            @RequestParam(required = false) String otp2,
            @RequestParam(required = false) String otp3,
            @RequestParam(required = false) String otp4,
            @RequestParam(required = false) String otp5,
            @RequestParam(required = false) String otp6,
            Model model
    ) {

        // ghép OTP
        String otp = (otp1 == null ? "" : otp1) +
                (otp2 == null ? "" : otp2) +
                (otp3 == null ? "" : otp3) +
                (otp4 == null ? "" : otp4) +
                (otp5 == null ? "" : otp5) +
                (otp6 == null ? "" : otp6);

        // ===== STEP 1: VERIFY OTP =====
        if (password == null) {

            if (otp.length() != 6) {
                return "redirect:/auth/forget/otp";
            }

            if (authService.verifyOTP(email, otp)) {
                model.addAttribute("email", email);
                return "auth/forgot-password-reset";
            }

            return "redirect:/auth/forget/otp";
        }

        // ===== STEP 2: RESET PASSWORD =====
        if (password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            model.addAttribute("email", email);
            return "auth/forgot-password-reset";
        }

        authService.resetPassword(email, password);

        return "redirect:/auth/reset-password-success";
    }
}