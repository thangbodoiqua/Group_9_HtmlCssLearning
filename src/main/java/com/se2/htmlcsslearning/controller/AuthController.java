package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.dto.request.ForgotPasswordRequest;
import com.se2.htmlcsslearning.dto.request.ResetPasswordRequest;
import com.se2.htmlcsslearning.dto.request.SignUpRequest;
import com.se2.htmlcsslearning.dto.request.VerifyOtpRequest;
import com.se2.htmlcsslearning.exception.EmailAlreadyExistsException;
import com.se2.htmlcsslearning.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    // ================= PAGE =================
    private boolean isLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/signin")
    public String signinPage(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,
                             Authentication auth,
                             Model model) {
        if (isLoggedIn(auth)) return "redirect:/";
        if (error != null) {
            model.addAttribute("error", "Invalid email or password.");
            model.asMap().remove("message");
        }
        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");
            model.asMap().remove("error");
        }
        return "auth/signin";
    }

    @GetMapping("/signup")
    public String signupPage(Authentication auth, Model model) {
        if (isLoggedIn(auth)) return "redirect:/";
        if (!model.containsAttribute("signUpRequest")) {
            model.addAttribute("signUpRequest", new SignUpRequest());
        }
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@Valid @ModelAttribute("signUpRequest") SignUpRequest request,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("confirmPassword")
                && request.getPassword() != null
                && !request.getPassword().equals(request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            String firstError = bindingResult.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("error", firstError);
            return "auth/signup";
        }

        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please sign in.");
            return "redirect:/auth/signin";
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/signup";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error. Please try again.");
            return "auth/signup";
        }
    }

    @GetMapping("/forget")
    public String forgotPage(Authentication auth, Model model) {
        if (isLoggedIn(auth)) return "redirect:/";
        if (!model.containsAttribute("forgotPasswordRequest")) {
            model.addAttribute("forgotPasswordRequest", new ForgotPasswordRequest());
        }
        return "auth/forgot-password";
    }

    @PostMapping("/forget")
    public String sendOtp(@Valid @ModelAttribute("forgotPasswordRequest") ForgotPasswordRequest request,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {
        if (bindingResult.hasErrors()) return "auth/forgot-password";

        try {
            authService.generateOTP(request.getEmail());
            session.setAttribute("resetEmail", request.getEmail());
            return "redirect:/auth/forget/otp";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/forgot-password";
        }
    }

    @PostMapping("/forget/otp")
    public String verifyOtp(@Valid @ModelAttribute("verifyOtpRequest") VerifyOtpRequest request,
                            BindingResult bindingResult,
                            HttpSession session,
                            Model model) {
        String email = (String) session.getAttribute("resetEmail");
        if (email == null) return "redirect:/auth/forget";

        if (bindingResult.hasErrors()) return "auth/forgot-password-otp";

        if (!authService.verifyOTP(email, request.getOtp())) {
            bindingResult.rejectValue("otp", "error.otp", "Incorrect or expired OTP.");
            return "auth/forgot-password-otp";
        }

        session.setAttribute("isVerified", true);
        return "redirect:/auth/forget/reset";
    }


    @GetMapping("/forget/reset")
    public String resetPage(Model model, HttpSession session) {
        if (session.getAttribute("isVerified") == null || !(Boolean) session.getAttribute("isVerified")) {
            return "redirect:/auth/forget";
        }
        if (!model.containsAttribute("resetPasswordRequest")) {
            model.addAttribute("resetPasswordRequest", new ResetPasswordRequest());
        }
        return "auth/forgot-password-reset";
    }


    @PostMapping("/forget/reset")
    public String resetPassword(@Valid @ModelAttribute("resetPasswordRequest") ResetPasswordRequest request,
                                BindingResult bindingResult,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("resetEmail");
        Boolean isVerified = (Boolean) session.getAttribute("isVerified");
        if (email == null || isVerified == null || !isVerified) return "redirect:/auth/forget";

        if (bindingResult.hasErrors()) return "auth/forgot-password-reset";

        try {
            authService.resetPassword(email, request.getNewPassword());
            session.invalidate();
            redirectAttributes.addFlashAttribute("successMessage", "Password reset successfully. Please sign in.");
            return "redirect:/auth/signin";
        } catch (Exception e) {
            bindingResult.reject("globalError", e.getMessage());
            return "auth/forgot-password-reset";
        }
    }

    @GetMapping("/reset-password-success")
    public String successPage() {
        return "auth/reset-password-success";
    }
}