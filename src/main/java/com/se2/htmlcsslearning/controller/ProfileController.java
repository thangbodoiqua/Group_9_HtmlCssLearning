package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public String profileManagement(Model model) {
        User currentProfile = profileService.getCurrentProfile();
        model.addAttribute("profile", currentProfile);
        return "profile/management";
    }
}
