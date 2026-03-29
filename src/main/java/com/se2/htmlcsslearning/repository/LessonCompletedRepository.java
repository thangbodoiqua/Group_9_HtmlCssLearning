package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.LessonCompleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCompletedRepository extends JpaRepository<LessonCompleted, Long> {
    boolean existsByLesson_IdAndUser_Id(Long lessonId, Integer userId);
}
