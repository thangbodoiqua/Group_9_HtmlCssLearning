package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.LessonNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonNoteRepository extends JpaRepository<LessonNote, Integer> {
    List<LessonNote> findByUser_IdAndLesson_Id(Integer userId, Long lessonId);
    Optional<LessonNote> findFirstByUser_IdAndLesson_Id(Integer userId, Long lessonId);
    List<LessonNote> findByUser_IdOrderByLesson_IdAsc(Integer userId);
}

