package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.se2.htmlcsslearning.dto.request.ChangePasswordRequest;
import com.se2.htmlcsslearning.dto.request.ProfileRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.Authenticator;


@Controller
@RequestMapping("/profile")
public class ProfileController {
  @Autowired
  private ProfileService profileService;

  @GetMapping
  public String profilePage(Model model) {
    if (!model.containsAttribute("profile")) {
      model.addAttribute("profile", profileService.getCurrentProfile());
    }
    return "profile/profile";
  }

  @GetMapping("/change-password")
  public String changePasswordView(Model model) {
    if (!model.containsAttribute("passwordRequest")) {
      model.addAttribute("passwordRequest", new ChangePasswordRequest());
    }
    return "profile/change-password";
  }

  @PostMapping("/update")
  public String handleUpdateProfile(@Valid @ModelAttribute("profile") ProfileRequest request,
                                    BindingResult bindingResult,
                                    RedirectAttributes ra) {
    if (bindingResult.hasErrors()) {
      return "redirect:/profile/profile";
    }
    profileService.updateProfile(request);
    ra.addFlashAttribute("message", "Profile updated successfully.");
    return "redirect:/profile";
  }

  @PostMapping("/change-password")
  public String handleChangePassword(@Valid @ModelAttribute("passwordRequest") ChangePasswordRequest request,
                                     BindingResult bindingResult,
                                     RedirectAttributes ra, Model model) {
    if (request.getNewPassword() != null && !request.getNewPassword().equals(request.getConfirmPassword())) {
      bindingResult.rejectValue("confirmPassword", "error.mismatch", "Passwords do not match");
    }

    if (bindingResult.hasErrors()) return "redirect:/profile/change-password";

    if (profileService.changePassword(request)) {
      return "redirect:/auth/logout";
    } else {
      model.addAttribute("error", "Incorrect current password.");
      return "redirect:/profile/change-password";
    }
  }
  }