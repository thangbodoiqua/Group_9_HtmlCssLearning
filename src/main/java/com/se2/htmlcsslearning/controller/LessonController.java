//package com.se2.htmlcsslearning.controller;
//
//public class LessonController {
//
//}
package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.entity.Lesson;
import com.se2.htmlcsslearning.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{category}/{lessonName}")
    public String showLessonPage(@PathVariable String category,
            @PathVariable String lessonName,
            Model model) {

        // 1. Chuyển category thành chữ in hoa (HTML hoặc CSS)
        String categoryDbName = category.toUpperCase();

        // 2. Lấy danh sách bài học từ DB để làm Sidebar
        List<Lesson> sidebarLessons = lessonService.getLessonsByCategory(categoryDbName);

        // --- LOGIC XỬ LÝ NÚT NEXT VÀ PREVIOUS ---
        String prevLessonUrl = "#";
        String nextLessonUrl = "#";

        for (int i = 0; i < sidebarLessons.size(); i++) {
            if (sidebarLessons.get(i).getLessonName().equals(lessonName)) {
                // Nếu có bài học trước đó
                if (i > 0) {
                    prevLessonUrl = "/lesson/" + category.toLowerCase() + "/"
                            + sidebarLessons.get(i - 1).getLessonName();
                }
                // Nếu có bài học sau đó
                if (i < sidebarLessons.size() - 1) {
                    nextLessonUrl = "/lesson/" + category.toLowerCase() + "/"
                            + sidebarLessons.get(i + 1).getLessonName();
                }
                break; // Tìm thấy bài hiện tại rồi thì thoát vòng lặp
            }
        }

        // 3. Gửi toàn bộ dữ liệu này sang cho Thymeleaf
        model.addAttribute("lessons", sidebarLessons);
        model.addAttribute("currentCategory", categoryDbName);
        model.addAttribute("currentLessonName", lessonName);
        model.addAttribute("prevLessonUrl", prevLessonUrl);
        model.addAttribute("nextLessonUrl", nextLessonUrl);

        // 4. Trả về đúng template bài học theo tên từ URL hoặc theo tên có prefix
        // category.
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

    // Tự động chuyển hướng khi người dùng chỉ bấm /lesson/html hoặc /lesson/css
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