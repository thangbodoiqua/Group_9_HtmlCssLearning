package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.entity.Lesson;
import com.se2.htmlcsslearning.entity.LessonNote;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.repository.LessonNoteRepository;
import com.se2.htmlcsslearning.repository.LessonRepository;
import com.se2.htmlcsslearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private LessonNoteRepository lessonNoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping
    public ResponseEntity<?> getNote(@RequestParam Integer userId, @RequestParam Long lessonId) {
        return lessonNoteRepository.findFirstByUser_IdAndLesson_Id(userId, lessonId)
                .map(note -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("content", note.getNoteContent());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<?> saveNote(@RequestBody Map<String, Object> payload) {
        try {
            Integer userId = (Integer) payload.get("userId");
            Object lessonIdObj = payload.get("lessonId");
            Long lessonId = (lessonIdObj instanceof Integer) ? ((Integer) lessonIdObj).longValue() : Long.valueOf(lessonIdObj.toString());
            String content = (String) payload.get("content");

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found"));

            LessonNote note = lessonNoteRepository.findFirstByUser_IdAndLesson_Id(userId, lessonId)
                    .orElse(new LessonNote());
            
            note.setUser(user);
            note.setLesson(lesson);
            note.setNoteContent(content);

            LessonNote savedNote = lessonNoteRepository.save(note);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("content", savedNote.getNoteContent());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Integer id) {
        try {
            lessonNoteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
