package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.LessonNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonNoteRepository extends JpaRepository<LessonNote, Integer> {
}

