package com.se2.htmlcsslearning.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.se2.htmlcsslearning.controller.dto.ChallengeResponse;
import com.se2.htmlcsslearning.controller.dto.ChallengeSubmissionRequest;
import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.entity.ChallengeSubmission;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.repository.UserRepository;
import com.se2.htmlcsslearning.repository.ChallengeRepository;
import com.se2.htmlcsslearning.repository.ChallengeSubmissionRepository;
import com.se2.htmlcsslearning.service.ChallengeService;
import com.se2.htmlcsslearning.service.GradingService;
import com.se2.htmlcsslearning.service.dto.GradingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChallengeServiceImpl implements ChallengeService {

    private static final Logger logger = LoggerFactory.getLogger(ChallengeServiceImpl.class);

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeSubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GradingService gradingService;

    @Override
    public ChallengeResponse processChallengeResponse(ChallengeSubmissionRequest request) {
        // 1. Get Challenge details
        Challenge challenge = challengeRepository.findById(request.getChallengeId().intValue())
                .orElseThrow(() -> new RuntimeException("Challenge not found"));

        // 2. Delegate Technical Evaluation to GradingService (Modular/Clean)
        GradingResult result = gradingService.evaluate(challenge, request.getUserCode(), request.getModelId());

        try {
            // 3. Save Submission (Persistence Management)
            ChallengeSubmission submission = new ChallengeSubmission();
            submission.setChallenge(challenge);
            submission.setSubmission(request.getUserCode());
            submission.setScore(result.getScore());
            submission.setFeedback(result.getFeedback());
            submission.setCreateAt(LocalDateTime.now());

            // For now, assign to first user if authentication is not ready
            User defaultUser = userRepository.findAll().stream().findFirst().orElse(null);
            if (defaultUser == null) {
                logger.error("No user found in database.");
                throw new RuntimeException("No user found in database. Submission cannot be saved.");
            }
            submission.setUser(defaultUser);

            submission = submissionRepository.save(submission);
            logger.info("Submission saved successfully with ID: {}", submission.getId());

            return ChallengeResponse.builder()
                    .success(true)
                    .score(result.getScore())
                    .feedback(result.getFeedback())
                    .submissionId(submission.getId().longValue())
                    .message("Evaluation completed successfully!")
                    .build();

        } catch (Exception e) {
            logger.error("Failed to save submission: ", e);
            throw new RuntimeException("Save failed: " + e.getMessage());
        }
    }
}
