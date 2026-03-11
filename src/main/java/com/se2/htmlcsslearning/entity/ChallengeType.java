package com.se2.htmlcsslearning.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class ChallengeType {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "challenge_type", nullable = false, unique=true)
    private String challengeType;

    @OneToMany(mappedBy = "challengeType")
    private List<Challenge> challenges;
}
