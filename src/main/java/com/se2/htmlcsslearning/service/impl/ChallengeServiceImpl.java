package com.se2.htmlcsslearning.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.se2.htmlcsslearning.dto.response.ChallengeResponse;
import com.se2.htmlcsslearning.dto.response.ChallengeSubmissionRequest;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Challenge> getChallengesByType(String type) {
        return challengeRepository.findAll().stream()
                .filter(c -> type.equalsIgnoreCase(c.getChallengeType().getChallengeType()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Challenge> getChallengeByTitle(String title) {
        return challengeRepository.findByChallengeTitle(title);
    }

    @Override
    public ChallengeSubmission getSubmissionById(Integer id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    @Override
    public ChallengeResponse executeGrading(ChallengeSubmissionRequest request) {
        // 1. Get Challenge details
        Challenge challenge = challengeRepository.findByChallengeTitle(request.getChallengeTitle())
                .orElseThrow(() -> new RuntimeException("Challenge not found"));

        String type = challenge.getChallengeType().getChallengeType();
        String codeToEvaluate = request.getUserCode();

        GradingResult result = gradingService.evaluate(challenge, codeToEvaluate, request.getModelId());

        try {
            // 3. Save Submission (Persistence Management)
            ChallengeSubmission submission = new ChallengeSubmission();
            submission.setChallenge(challenge);
            submission.setSubmission(codeToEvaluate);
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
