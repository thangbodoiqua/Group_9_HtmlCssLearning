package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.entity.LessonNote;
import com.se2.htmlcsslearning.repository.LessonCompletedRepository;
import com.se2.htmlcsslearning.repository.LessonNoteRepository;
import com.se2.htmlcsslearning.repository.LessonRepository;
import com.se2.htmlcsslearning.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressServiceImpl implements ProgressService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonCompletedRepository lessonCompletedRepository;

    @Autowired
    private LessonNoteRepository lessonNoteRepository;

    @Override
    public long getTotalLessons() {
        return lessonRepository.count();
    }

    @Override
    public long getCompletedLessons(Integer userId) {
        return lessonCompletedRepository.countByUserId(userId);
    }

    @Override
    public int getHtmlPercent(Integer userId) {
        long totalHtml = lessonRepository.countByCategory_CategoryName("HTML");
        long completedHtml = lessonCompletedRepository.countByUserIdAndLessonCategoryCategoryName(userId, "HTML");
        return (totalHtml > 0) ? (int) ((completedHtml * 100) / totalHtml) : 0;
    }

    @Override
    public int getCssPercent(Integer userId) {
        long totalCss = lessonRepository.countByCategory_CategoryName("CSS");
        long completedCss = lessonCompletedRepository.countByUserIdAndLessonCategoryCategoryName(userId, "CSS");
        return (totalCss > 0) ? (int) ((completedCss * 100) / totalCss) : 0;
    }

    @Override
    public List<LessonNote> getUserNotes(Integer userId) {
        return lessonNoteRepository.findByUser_IdOrderByLesson_IdAsc(userId);
    }
}

