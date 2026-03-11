package com.se2.htmlcsslearning.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="challenge_type", referencedColumnName = "challange_type")
    private ChallengeType challengeType;

    @Column(name = "challenge_title")
    private String challengeTitle;

    @Column(name = "challenge_difficulty")
    private String challengeDifficulty;

    @Column(name = "challenge_hints")
    private String challengeHints;

    @Column(name = "challenge_instructions")
    private String challengeInstructions;
}
