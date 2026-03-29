package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.controller.dto.ChallengeResponse;
import com.se2.htmlcsslearning.controller.dto.ChallengeSubmissionRequest;
import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.entity.ChallengeSubmission;

import java.util.List;
import java.util.Optional;

public interface ChallengeService {
    ChallengeResponse executeGrading(ChallengeSubmissionRequest request);

    List<Challenge> getChallengesByType(String type);

    Optional<Challenge> getChallengeByTitle(String title);

    ChallengeSubmission getSubmissionById(Integer id);
}
