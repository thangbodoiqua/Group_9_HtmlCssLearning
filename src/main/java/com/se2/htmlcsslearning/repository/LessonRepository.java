//package com.se2.htmlcsslearning.repository;
//
//import com.se2.htmlcsslearning.entity.Lesson;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface LessonRepository extends JpaRepository<Lesson, Long> {
//}
//
package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCategory_CategoryNameOrderByLessonOrderAsc(String categoryName);

    long countByCategory_CategoryName(String categoryName);
}