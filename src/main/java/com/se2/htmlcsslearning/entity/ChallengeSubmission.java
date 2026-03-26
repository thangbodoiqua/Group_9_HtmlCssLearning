package com.se2.htmlcsslearning.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ChallengeSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="challenge_id", nullable=false)
    private Challenge challenge;

    @Column(name="submission", columnDefinition = "TEXT")
    private String submission;

    @Column(name="score", nullable=false)
    private Integer score;

    @Column(name="feedback", nullable=false, columnDefinition = "TEXT")
    private String feedback;

    @Column(name="created_at")
    private LocalDateTime createAt;

}
