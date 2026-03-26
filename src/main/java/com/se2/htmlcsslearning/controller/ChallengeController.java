package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.controller.dto.ChallengeResponse;
import com.se2.htmlcsslearning.controller.dto.ChallengeSubmissionRequest;
import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.entity.ChallengeSubmission;
import com.se2.htmlcsslearning.repository.ChallengeRepository;
import com.se2.htmlcsslearning.repository.ChallengeSubmissionRepository;
import com.se2.htmlcsslearning.service.ChallengeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/practice")
public class ChallengeController {
    // log error chi tiet
    private static final Logger logger = LoggerFactory.getLogger(ChallengeController.class);

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeSubmissionRepository submissionRepository;

    @GetMapping("/pixel-perfect")
    public String showPixelPerfectPage(Model model) {
        List<Challenge> challenges = challengeRepository.findAll();
        Challenge first = challenges.stream()
                .filter(c -> "PIXEL_PERFECT".equalsIgnoreCase(c.getChallengeType().getChallengeType()))
                .findFirst().orElse(null);

        model.addAttribute("challenge", first);
        return "challenge/practice-pixel-perfect";
    }

    @GetMapping("/css-debug")
    public String showCssDebugPage(Model model) {
        List<Challenge> challenges = challengeRepository.findAll();
        Challenge first = challenges.stream()
                .filter(c -> "CSS_DEBUG".equalsIgnoreCase(c.getChallengeType().getChallengeType()))
                .findFirst().orElse(null);

        model.addAttribute("challenge", first);
        return "challenge/practice-css-debug";
    }

    @GetMapping("/result/{id}")
    public String showResultPage(@PathVariable Integer id, Model model) {
        ChallengeSubmission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        model.addAttribute("submission", submission);

        String type = submission.getChallenge().getChallengeType().getChallengeType();
        if ("PIXEL_PERFECT".equalsIgnoreCase(type)) {
            return "challenge/ai-grade-pixel-perfect";
        } else {
            return "challenge/ai-grade-css-debug";
        }
    }

    @PostMapping("/api/submit")
    @ResponseBody
    public ResponseEntity<ChallengeResponse> submitChallenge(@RequestBody ChallengeSubmissionRequest request) {
        try {
            ChallengeResponse response = challengeService.processChallengeResponse(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Submission failed: ", e);
            return ResponseEntity.status(500).body(ChallengeResponse.builder()
                    .success(false)
                    .message("Evaluation failed: " + e.getMessage())
                    .build());
        }
    }
}
