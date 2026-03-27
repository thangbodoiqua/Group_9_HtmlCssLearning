package com.se2.htmlcsslearning.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name="user_role", nullable=false)
    private String userRole;

    @Column(name="user_name", nullable=false)
    private String userName;

    @Column(name="dob", nullable=false)
    private LocalDate dob;

    @Column(name="reg_date", nullable=false)
    private LocalDateTime regDate;

    @Column(name="password_hash", nullable=false)
    private String password;

    /*@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    private List<LessonCompleted> lessonCompleteds;

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    private List<LessonNote> lessonNotes;

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    private List<ChallengeSubmission> challengeSubmissions;*/
}
