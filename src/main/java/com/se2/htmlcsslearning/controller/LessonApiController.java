package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.entity.Lesson;
import com.se2.htmlcsslearning.entity.LessonCompleted;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.repository.LessonCompletedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/lesson")
public class LessonApiController {

    @Autowired
    private LessonCompletedRepository completedRepository;

    @PostMapping("/mark-completed")
    public ResponseEntity<?> markAsCompleted(@RequestParam Long lessonId, @RequestParam Integer userId) {

        if (completedRepository.existsByLesson_IdAndUser_Id(lessonId, userId)) {
            return ResponseEntity.badRequest().body("Already completed");
        }

        LessonCompleted completed = new LessonCompleted();

        Lesson lessonRef = new Lesson();
        lessonRef.setId(lessonId);
        completed.setLesson(lessonRef);

        User userRef = new User();
        userRef.setId(userId);
        completed.setUser(userRef);

        completed.setCompletedAt(LocalDateTime.now());
        completedRepository.save(completed);

        return ResponseEntity.ok("Success");
    }
}