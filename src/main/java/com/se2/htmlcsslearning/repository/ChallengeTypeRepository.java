package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.ChallengeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeTypeRepository extends JpaRepository<ChallengeType, Integer> {
}

