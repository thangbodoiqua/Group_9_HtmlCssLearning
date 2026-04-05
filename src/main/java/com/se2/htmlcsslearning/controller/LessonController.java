//package com.se2.htmlcsslearning.controller;
//
//public class LessonController {
//
//}
package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.entity.Lesson;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.repository.LessonCompletedRepository;
import com.se2.htmlcsslearning.repository.LessonNoteRepository;
import com.se2.htmlcsslearning.repository.UserRepository;
import com.se2.htmlcsslearning.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private LessonCompletedRepository lessonCompletedRepository;

    @Autowired
    private LessonNoteRepository lessonNoteRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{category}/{lessonName}")
    public String showLessonPage(@PathVariable String category,
            @PathVariable String lessonName,
            Model model,
            Authentication authentication) {

        String categoryDbName = category.toUpperCase();

        List<Lesson> sidebarLessons = lessonService.getLessonsByCategory(categoryDbName);

        Lesson currentLesson = null;
        for (Lesson lesson : sidebarLessons) {
            if (lesson.getLessonName().equals(lessonName)) {
                currentLesson = lesson;
                break;
            }
        }

        if (currentLesson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found");
        }

        Long currentLessonId = currentLesson.getId();

        String prevLessonUrl = "#";
        String nextLessonUrl = "#";

        for (int i = 0; i < sidebarLessons.size(); i++) {
            if (sidebarLessons.get(i).getLessonName().equals(lessonName)) {
                if (i > 0) {
                    prevLessonUrl = "/lesson/" + category.toLowerCase() + "/"
                            + sidebarLessons.get(i - 1).getLessonName();
                }
                if (i < sidebarLessons.size() - 1) {
                    nextLessonUrl = "/lesson/" + category.toLowerCase() + "/"
                            + sidebarLessons.get(i + 1).getLessonName();
                }
                break;
            }
        }

        boolean isCompleted = false;
        String currentNote = "";
        Integer userId = 1;
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                userId = user.getId();
                isCompleted = lessonCompletedRepository.existsByLessonIdAndUserId(currentLessonId, userId);
                
                // Lấy ghi chú hiện tại
                currentNote = lessonNoteRepository.findFirstByUser_IdAndLesson_Id(userId, currentLessonId)
                        .map(note -> note.getNoteContent())
                        .orElse("");
            }
        }

        model.addAttribute("lessons", sidebarLessons);
        model.addAttribute("currentLesson", currentLesson);
        model.addAttribute("currentLessonId", currentLessonId);
        model.addAttribute("isCompleted", isCompleted);
        model.addAttribute("userId", userId);
        model.addAttribute("currentNote", currentNote);
        model.addAttribute("currentCategory", categoryDbName);
        model.addAttribute("currentLessonName", lessonName);
        model.addAttribute("prevLessonUrl", prevLessonUrl);
        model.addAttribute("nextLessonUrl", nextLessonUrl);

        return resolveLessonTemplateView(category, lessonName);
    }

    private String resolveLessonTemplateView(String category, String lessonName) {
        String basePath = "lesson/" + category.toLowerCase() + "/";

        String directView = basePath + lessonName;
        if (templateExists(directView)) {
            return directView;
        }

        String prefixedView = basePath + category.toUpperCase() + "_" + lessonName;
        if (templateExists(prefixedView)) {
            return prefixedView;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Lesson template not found for category='" + category + "', lesson='" + lessonName + "'");
    }

    private boolean templateExists(String viewName) {
        Resource template = resourceLoader.getResource("classpath:templates/" + viewName + ".html");
        return template.exists();
    }

    @GetMapping("/{category}")
    public String redirectFirstLesson(@PathVariable String category) {
        if (category.equalsIgnoreCase("html")) {
            return "redirect:/lesson/html/HTML-Introduction";
        } else if (category.equalsIgnoreCase("css")) {
            return "redirect:/lesson/css/CSS-Introduction";
        }
        return "redirect:/";
    }
}