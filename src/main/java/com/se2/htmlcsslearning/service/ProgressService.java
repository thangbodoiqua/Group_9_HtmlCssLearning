package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.entity.LessonNote;
import java.util.List;

public interface ProgressService {
    long getTotalLessons();
    long getCompletedLessons(Integer userId);
    int getHtmlPercent(Integer userId);
    int getCssPercent(Integer userId);
    List<LessonNote> getUserNotes(Integer userId);
}
