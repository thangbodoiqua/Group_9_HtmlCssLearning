package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.dto.request.ForgotPasswordRequest;
import com.se2.htmlcsslearning.dto.request.ResetPasswordRequest;
import com.se2.htmlcsslearning.dto.request.SignUpRequest;
import com.se2.htmlcsslearning.dto.request.VerifyOtpRequest;
import com.se2.htmlcsslearning.exception.EmailAlreadyExistsException;
import com.se2.htmlcsslearning.service.AuthService;
import com.se2.htmlcsslearning.utils.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping("/signin")
    public String signinPage(@RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "logout", required = false) String logout,
                             Model model) {
        if (SecurityUtil.isAuthenticated()) return "redirect:/";
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
    public String signupPage(Model model) {
        if (SecurityUtil.isAuthenticated()) return "redirect:/";
        if (!model.containsAttribute("signUpRequest")) {
            model.addAttribute("signUpRequest", new SignUpRequest());
        }
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@Validated(SignUpRequest.ValidationOrder.class) @ModelAttribute("signUpRequest") SignUpRequest request,
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
    public String forgotPage(Model model) {
        if (SecurityUtil.isAuthenticated()) return "redirect:/";
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

        if (bindingResult.hasErrors()) {
            String firstError = bindingResult.getAllErrors().getFirst().getDefaultMessage();
            model.addAttribute("error", firstError);
            return "auth/forgot-password";
        }

        try {
            authService.sendOtp(request);
            session.setAttribute("resetEmail", request.getEmail());
            return "redirect:/auth/forget/otp";
        }catch (UsernameNotFoundException e){
            model.addAttribute("error", "Email not found");
            return "auth/forgot-password";
        } catch (Exception e) {
            model.addAttribute("error", "Send otp failed, please try again");
            return "auth/forgot-password";
        }
    }

    @GetMapping("/forget/otp")
    public String forgotOtpPage() {

        return "auth/forgot-password-otp";
    }

    @PostMapping("/forget/otp")
    public String verifyOtp(@Valid @ModelAttribute("verifyOtpRequest") VerifyOtpRequest request,
                            BindingResult bindingResult,
                            HttpSession session,
                            Model model) {

        if (bindingResult.hasErrors()) {
            String firstError = bindingResult.getAllErrors().getFirst().getDefaultMessage();
            model.addAttribute("error", firstError);
            return "auth/forgot-password-otp";
        }

        String email = (String) session.getAttribute("resetEmail");

        if (email == null) {
            return "redirect:/auth/forget";
        }

        request.setEmail(email);

        if (authService.verifyOTP(request)) {
            session.setAttribute("isVerified", true);
            return "redirect:/auth/forget/reset";
        } else {
            model.addAttribute("error", "Incorrect or expired OTP.");
            return "auth/forgot-password-otp";
        }
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
                                Model model) {
        if (bindingResult.hasErrors()) {
            String firstError = bindingResult.getAllErrors().getFirst().getDefaultMessage();
            model.addAttribute("error", firstError);
            return "auth/forgot-password-reset";
        }

        if (!request.getConfirmPassword().equals(request.getNewPassword())) {
            model.addAttribute("error", "Confirm  password is not same new password");
            return "auth/forgot-password-reset";
        }

        String email = (String) session.getAttribute("resetEmail");
        Boolean isVerified = (Boolean) session.getAttribute("isVerified");

        if (email == null || isVerified == null) return "redirect:/auth/forget";

        try {
            authService.resetPassword(request);
            session.removeAttribute("resetEmail");
            return "redirect:/auth/reset-password-success";
        } catch (Exception e) {
            bindingResult.reject("globalError", "Cannot reset password, please try again");
            return "auth/forgot-password-reset";
        }
    }

    @GetMapping("/reset-password-success")
    public String successPage() {
        return "auth/reset-password-success";
    }
}