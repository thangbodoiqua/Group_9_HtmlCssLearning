//package com.se2.htmlcsslearning.service.impl;
//
//import com.se2.htmlcsslearning.service.LessonService;
//import org.springframework.stereotype.Service;
//
//@Service
//public class LessonServiceImpl implements LessonService {
//}
//
package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.entity.Lesson;
import com.se2.htmlcsslearning.repository.LessonRepository;
import com.se2.htmlcsslearning.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<Lesson> getLessonsByCategory(String categoryName) {
        return lessonRepository.findByCategory_CategoryNameOrderByLessonOrderAsc(categoryName);
    }
}