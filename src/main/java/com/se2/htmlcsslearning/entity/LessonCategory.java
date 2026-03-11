package com.se2.htmlcsslearning.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class LessonCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, unique=true)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Lesson> lessons;
}
