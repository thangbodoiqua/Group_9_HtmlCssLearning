package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.ChallengeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeSubmissionRepository extends JpaRepository<ChallengeSubmission, Integer> {
}

