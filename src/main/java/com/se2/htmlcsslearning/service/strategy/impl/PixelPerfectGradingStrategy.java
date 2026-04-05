package com.se2.htmlcsslearning.service.strategy.impl;

import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.service.builder.AiGradingPromptBuilder;
import com.se2.htmlcsslearning.service.dto.GradingContext;
import com.se2.htmlcsslearning.service.strategy.GradingStrategy;

public class PixelPerfectGradingStrategy implements GradingStrategy {

    @Override
    public String generatePrompt(Challenge challenge, String userCode, GradingContext context) {
        AiGradingPromptBuilder builder = new AiGradingPromptBuilder();

        String systemInstructions = "You are a specialized CSS Design Reviewer. Your goal is to evaluate if the student's code achieves the same design outcome as the target reference solution. \n"
                +
                "IMPORTANT: Focus on the 'Outcome' and 'Methodology'. Accept any valid CSS technique as long as the result matches the design goals.";

        String evaluationRules = String.format(
                "1. Reference Design Spec (The Target Solution CSS):\n```css\n%s\n```\n" +
                        "2. GRADING PHILOSOPHY (Strict Outcome-Based):\n" +
                        "   - Your primary goal is to evaluate if the student's code achieves the SAME VISUAL RESULT as the reference.\n" +
                        "   - CRITICAL: Look into the student's <style> tag or CSS block. If it is EMPTY, carries ONLY comments, or lacks the necessary properties to achieve the target, the score MUST be 0.\n" +
                        "   - DO NOT hallucinate a correct result if the CSS properties are missing.\n" +
                        "   - IMPORTANT: Only accept valid CSS techniques. If the student achieve the result with different properties (e.g. Flexbox vs Grid), it's fine, but the properties MUST be present.\n" +
                        "   - Determine the completion percentage based on how much of the target design has been SUCCESSFULLY REPRODUCED by the CSS.\n" +
                        "3. OUTPUT RULE: Provide feedback on what is missing and include the final 'score' (0-100) in the JSON.",
                context.getReferenceCss());

        return builder
                .addSystemInstructions(systemInstructions)
                .addEvaluationRules(evaluationRules)
                .addFeedbackTemplate()
                .addUserCode(userCode)
                .addTargetDescriptions("Pixel Perfect Challenge: " + challenge.getChallengeTitle())
                .addOutputFormat()
                .build();
    }
}
