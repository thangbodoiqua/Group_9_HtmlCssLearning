package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.entity.LessonNote;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.repository.LessonCompletedRepository;
import com.se2.htmlcsslearning.repository.LessonNoteRepository;
import com.se2.htmlcsslearning.repository.LessonRepository;
import com.se2.htmlcsslearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProgressController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonCompletedRepository lessonCompletedRepository;

    @Autowired
    private LessonNoteRepository lessonNoteRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/progress")
    public String showProgressPage(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/signin";
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/auth/signin";
        }

        Integer userId = user.getId();

        // 1. Total counting
        long totalLessons = lessonRepository.count();
        long completedLessons = lessonCompletedRepository.countByUser_Id(userId);

        // 2. Category progress
        long totalHtml = lessonRepository.countByCategory_CategoryName("HTML");
        long completedHtml = lessonCompletedRepository.countByUser_IdAndLesson_Category_CategoryName(userId, "HTML");
        int htmlPercent = (totalHtml > 0) ? (int) ((completedHtml * 100) / totalHtml) : 0;

        long totalCss = lessonRepository.countByCategory_CategoryName("CSS");
        long completedCss = lessonCompletedRepository.countByUser_IdAndLesson_Category_CategoryName(userId, "CSS");
        int cssPercent = (totalCss > 0) ? (int) ((completedCss * 100) / totalCss) : 0;

        // 3. Saved notes
        List<LessonNote> userNotes = lessonNoteRepository.findByUser_IdOrderByLesson_IdAsc(userId);

        // 4. Add to model
        model.addAttribute("user", user);
        model.addAttribute("totalCount", totalLessons);
        model.addAttribute("completedCount", completedLessons);
        model.addAttribute("htmlPercent", htmlPercent);
        model.addAttribute("cssPercent", cssPercent);
        model.addAttribute("notes", userNotes);

        return "progress";
    }
}
