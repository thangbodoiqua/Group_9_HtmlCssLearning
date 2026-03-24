//package com.se2.htmlcsslearning.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;

//@Entity
//@Data
//public class Lesson {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "lesson_name")
//    private String lessonName;
//
//    @Column(name = "lesson_order")
//    private Integer lessonOrder;
//
//    @ManyToOne
//    @JoinColumn(name="category_name", referencedColumnName = "category_name")
//    private LessonCategory category;
//}
package com.se2.htmlcsslearning.entity;

import jakarta.persistence.*;

@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_name")
    private String lessonName;

    @Column(name = "lesson_order")
    private Integer lessonOrder;

    @ManyToOne
    @JoinColumn(name="category_name", referencedColumnName = "category_name")
    private LessonCategory category;

    // --- BẮT ĐẦU THÊM GETTER VÀ SETTER THỦ CÔNG ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Integer getLessonOrder() {
        return lessonOrder;
    }

    public void setLessonOrder(Integer lessonOrder) {
        this.lessonOrder = lessonOrder;
    }

    public LessonCategory getCategory() {
        return category;
    }

    public void setCategory(LessonCategory category) {
        this.category = category;
    }
}