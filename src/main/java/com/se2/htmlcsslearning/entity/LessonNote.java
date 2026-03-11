package com.se2.htmlcsslearning.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LessonNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "note_content")
    private String noteContent;

}
