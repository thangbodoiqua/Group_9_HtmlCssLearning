//package com.se2.htmlcsslearning.service;
//
//public interface LessonService {
//}
package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.entity.Lesson;
import java.util.List;

public interface LessonService {
    List<Lesson> getLessonsByCategory(String categoryName);
}