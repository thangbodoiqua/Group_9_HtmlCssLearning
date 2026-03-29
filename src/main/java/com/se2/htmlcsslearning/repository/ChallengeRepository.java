package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    Optional<Challenge> findByChallengeTitle(String challengeTitle);
}
