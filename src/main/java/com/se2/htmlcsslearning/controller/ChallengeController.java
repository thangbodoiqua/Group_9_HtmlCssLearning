package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.dto.response.ChallengeResponse;
import com.se2.htmlcsslearning.dto.response.ChallengeSubmissionRequest;
import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.entity.ChallengeSubmission;
import com.se2.htmlcsslearning.service.ChallengeService;
import com.se2.htmlcsslearning.service.util.ResourceReaderUtil;
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
    private static final Logger logger = LoggerFactory.getLogger(ChallengeController.class);

    @Autowired
    private ChallengeService challengeService;

    @GetMapping("/pixel-perfect")
    public String defaultPixelPerfect() {
        List<Challenge> challenges = challengeService.getChallengesByType("PIXEL_PERFECT");
        if (!challenges.isEmpty()) {
            return "redirect:/practice/pixel-perfect/" + challenges.get(0).getChallengeTitle();
        }
        return "redirect:/"; // fallback
    }

    @GetMapping("/pixel-perfect/{challengeTitle}")
    public String showPixelPerfectPage(@PathVariable String challengeTitle, Model model) {
        Challenge challenge = challengeService.getChallengeByTitle(challengeTitle)
                .orElseThrow(() -> new RuntimeException("Not found"));
        List<Challenge> allChallenges = challengeService.getChallengesByType("PIXEL_PERFECT");

        model.addAttribute("challenge", challenge);
        model.addAttribute("allChallenges", allChallenges);

        try {
            String initialHtml = ResourceReaderUtil.readFile("pixel-perfect", challenge.getChallengeTitle(), "index.html");
            String solutionCss = ResourceReaderUtil.readFile("pixel-perfect", challenge.getChallengeTitle(), "solution.css");
            String initialCss = "";
            try {
                initialCss = ResourceReaderUtil.readFile("pixel-perfect", challenge.getChallengeTitle(), "style.css");
            } catch (Exception e) {
                initialCss = "/* Add your CSS here */";
            }
            String solutionCombinedHtml;
            if (initialHtml.contains("</head>")) {
                solutionCombinedHtml = initialHtml.replace("</head>", "<style>\n" + solutionCss + "\n</style>\n</head>");
            } else {
                solutionCombinedHtml = "<style>\n" + solutionCss + "\n</style>\n" + initialHtml;
            }
            model.addAttribute("initialHtml", initialHtml.trim());
            model.addAttribute("initialCss", initialCss);
            model.addAttribute("solutionCombinedHtml", solutionCombinedHtml);
        } catch (Exception e) {
            model.addAttribute("initialHtml", "<!-- No sample HTML found -->");
            model.addAttribute("initialCss", "/* No sample CSS found */");
            model.addAttribute("solutionCombinedHtml", "<!-- No solution found -->");
        }

        return "challenge/practice-pixel-perfect";
    }

    @GetMapping("/css-debug")
    public String defaultCssDebug() {
        List<Challenge> challenges = challengeService.getChallengesByType("CSS_DEBUG");
        if (!challenges.isEmpty()) {
            return "redirect:/practice/css-debug/" + challenges.get(0).getChallengeTitle();
        }
        return "redirect:/"; // fallback
    }

    @GetMapping("/css-debug/{challengeTitle}")
    public String showCssDebugPage(@PathVariable String challengeTitle, Model model) {
        Challenge challenge = challengeService.getChallengeByTitle(challengeTitle)
                .orElseThrow(() -> new RuntimeException("Not found"));
        List<Challenge> allChallenges = challengeService.getChallengesByType("CSS_DEBUG");

        model.addAttribute("challenge", challenge);
        model.addAttribute("allChallenges", allChallenges);

        try {
            String initialCode = ResourceReaderUtil.readFile("css-debug", challenge.getChallengeTitle(), "index.html");
            String solutionCss = ResourceReaderUtil.readFile("css-debug", challenge.getChallengeTitle(),
                    "solution.css");

            String initialCss = "";
            String initialHtml = initialCode;
            int styleStart = initialCode.indexOf("<style>");
            int styleEnd = initialCode.indexOf("</style>");
            String solutionCombinedHtml;

            if (styleStart != -1 && styleEnd != -1) {
                initialCss = initialCode.substring(styleStart + 7, styleEnd).trim();
                initialHtml = initialCode.substring(0, styleStart) + initialCode.substring(styleEnd + 8);
                solutionCombinedHtml = initialCode.substring(0, styleStart) + "<style>\n" + solutionCss + "\n</style>"
                        + initialCode.substring(styleEnd + 8);
            } else {
                try {
                    initialCss = ResourceReaderUtil.readFile("css-debug", challenge.getChallengeTitle(), "style.css");
                } catch (Exception e) {
                    initialCss = "/* There is an error on rendering initial css code, if you still want to continue, write all of the css code*/";
                }

                if (initialCode.contains("</head>")) {
                    solutionCombinedHtml = initialCode.replace("</head>",
                            "<style>\n" + solutionCss + "\n</style>\n</head>");
                } else {
                    solutionCombinedHtml = "<style>\n" + solutionCss + "\n</style>\n" + initialCode;
                }
            }

            model.addAttribute("initialHtml", initialHtml.trim());
            model.addAttribute("initialCss", initialCss);
            model.addAttribute("solutionCombinedHtml", solutionCombinedHtml);
        } catch (Exception e) {
            model.addAttribute("initialHtml", "<!-- No sample HTML found -->");
            model.addAttribute("initialCss", "/* No sample CSS found */");
            model.addAttribute("solutionCombinedHtml", "<!-- No solution found -->");
        }

        return "challenge/practice-css-debug";
    }

    @GetMapping("/result/{id}")
    public String showResultPage(@PathVariable Integer id, Model model) {
        ChallengeSubmission submission = challengeService.getSubmissionById(id);
        model.addAttribute("submission", submission);
        model.addAttribute("combinedHtml", submission.getSubmission());

        String type = submission.getChallenge().getChallengeType().getChallengeType();
        if ("PIXEL_PERFECT".equalsIgnoreCase(type)) {
            return "challenge/ai-grade-pixel-perfect";
        }
        return "challenge/ai-grade-css-debug";
    }

    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<ChallengeResponse> submitChallenge(@RequestBody ChallengeSubmissionRequest request) {
        try {
            ChallengeResponse response = challengeService.executeGrading(request);
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
