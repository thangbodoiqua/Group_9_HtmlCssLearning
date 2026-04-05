package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.entity.LessonNote;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.service.ProgressService;
import com.se2.htmlcsslearning.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/progress")
    public String showProgressPage(Model model) {

        User user = SecurityUtil.getCurrentUser();

        if (user == null) {
            return "redirect:/auth/signin";
        }

        Integer userId = user.getId();

        long totalLessons = progressService.getTotalLessons();
        long completedLessons = progressService.getCompletedLessons(userId);

        int htmlPercent = progressService.getHtmlPercent(userId);
        int cssPercent = progressService.getCssPercent(userId);

        List<LessonNote> userNotes = progressService.getUserNotes(userId);

        model.addAttribute("user", user);
        model.addAttribute("totalCount", totalLessons);
        model.addAttribute("completedCount", completedLessons);
        model.addAttribute("htmlPercent", htmlPercent);
        model.addAttribute("cssPercent", cssPercent);
        model.addAttribute("notes", userNotes);

        return "progress";
    }
}
