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
    // Tự động generate câu lệnh SQL SELECT ... WHERE category_name = ? ORDER BY lesson_order ASC
    List<Lesson> findByCategory_CategoryNameOrderByLessonOrderAsc(String categoryName);
}