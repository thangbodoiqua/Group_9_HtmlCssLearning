package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    // ================= PAGE =================
    @GetMapping("/signin")
    public String signin(  @RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout",required = false) String logout,
                           HttpSession session, Model model){
        if (error !=null) {
            model.addAttribute("error", "Email or password incorrect ");
        }
        if (logout != null) {
                model.addAttribute("message", "Sign in success");
        }
        String successMsg = (String) session.getAttribute("signupSuccess");
        if (successMsg != null) {
            model.addAttribute("message", successMsg);
            session.removeAttribute("signupSuccess");
        }
        return "auth/signin";
    }

    @GetMapping("/signup")
    public String signup(HttpSession session, Model model ) {
        model.addAttribute("emailError", session.getAttribute("emailError"));
        model.addAttribute("error", session.getAttribute("signupError"));

        session.removeAttribute("signupError");
        session.removeAttribute("emailError");

        return "auth/signup";
    }
    @GetMapping("/forget")
    public String forget(HttpSession session, String email ) {
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
            @RequestParam String dob, HttpSession session
    ) {
        if (name == null || name.trim().isEmpty()) {
            session.setAttribute("signupError", "Name is required");
            return "redirect:/auth/signup";
        }
        if (email == null || !email.matches("[A-Za-z0-9+_.-]+@(.+)$")) {
            session.setAttribute("emailError", "Invalid email");
            return "redirect:/auth/signup";
        }
        if (password == null || password.length() < 6) {
            session.setAttribute("signupError", "Password must be at least 6 characters");
            return "redirect:/auth/signup";
        }
        if (dob == null || dob.trim().isEmpty()) {
            session.setAttribute("signupError", "Date of birth is required");
            return "redirect:/auth/signup";
        }
        try {
            authService.register(name, email, password, dob);
            session.setAttribute("signupSuccess", "Registration successful, please log in!");
            return "redirect:/auth/signin";
        } catch (Exception e) {
            session.setAttribute("signupError", e.getMessage());
            return "redirect:/auth/signup";
        }
    }
    @PostMapping("/forget")
    public String sendOpt(@RequestParam String email, HttpSession session, Model model) {
        try {
            authService.generateOTP(email);
            session.setAttribute("resetEmail", email);
            return "auth/forgot-password-otp";
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            return "auth/forgot-password";
        }
    }
    @PostMapping("/forget/otp")
    public String handleVerifyOtp(@RequestParam String otp, HttpSession session, Model model) {
        String email = (String) session.getAttribute("resetEmail");

        if (authService.verifyOTP(email, otp)) {
            session.setAttribute("isVerified", true);
            return "redirect:/auth/forget/reset";
        } else {
            model.addAttribute("error", "Incorrect or expired OTP code (60 seconds)\n");
            return "auth/forgot-password-otp";
        }
    }
    @PostMapping("/forgot/reset")
    public String handleReset(@RequestParam String password, HttpSession session, Model model) {
        String email = (String) session.getAttribute("resetEmail");
        Boolean isVerified = (Boolean) session.getAttribute("isVerified");
        if (email == null || isVerified == null || !isVerified) {
            return "redirect:/auth/forget";
        }
        authService.resetPassword(email, password);
        session.invalidate();
        return "redirect:/auth/reset-password-success";
    }
}