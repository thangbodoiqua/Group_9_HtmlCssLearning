package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.LessonCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCategoryRepository extends JpaRepository<LessonCategory, Long> {
}

