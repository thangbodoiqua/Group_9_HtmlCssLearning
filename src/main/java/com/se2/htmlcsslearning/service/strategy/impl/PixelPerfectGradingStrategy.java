package com.se2.htmlcsslearning.service.strategy.impl;

import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.service.builder.AiGradingPromptBuilder;
import com.se2.htmlcsslearning.service.dto.GradingContext;
import com.se2.htmlcsslearning.service.strategy.GradingStrategy;

public class PixelPerfectGradingStrategy implements GradingStrategy {

    @Override
    public String generatePrompt(Challenge challenge, String userCode, GradingContext context) {
        AiGradingPromptBuilder builder = new AiGradingPromptBuilder();
        
        Double score = context.getVisualScore() != null ? context.getVisualScore() : 0.0;
        int finalScore = (int) Math.round(score);

        String systemInstructions = "You are a CSS Tutor. A visual pixel-diffing algorithm has ALREADY graded the student's work.\n" +
                "Your ONLY job is to explain WHY they received this score by analyzing their CSS against the Reference CSS.";
        
        String evaluationRules = String.format(
                "1. PRE-CALCULATED SCORE: The student achieved exactly %d/100 visual accuracy.\n" +
                "2. Reference Design Spec (CSS):\n```css\n%s\n```\n" +
                "3. Your task is to identify the CSS discrepancies that caused the missing %d points.\n" +
                "4. OUTPUT FORMAT REQUIREMENTS:\n" +
                "   - Set your 'score' field EXACTLY to %d.\n" +
                "   - Provide your explanation in the 'feedback' field.\n" +
                "   - CRITICAL JSON RULE: DO NOT use double quotes (\") anywhere inside the 'feedback' value. Use single quotes instead to avoid breaking the JSON parser. DO NOT use newlines inside the string, use \\n instead.",
                finalScore, context.getReferenceCss(), 100 - finalScore, finalScore
        );

        return builder
                .addSystemInstructions(systemInstructions)
                .addEvaluationRules(evaluationRules)
                .addUserCode(userCode)
                .addTargetDescriptions("Pixel Perfect Challenge: " + challenge.getChallengeTitle())
                .addOutputFormat()
                .build();
    }
}
