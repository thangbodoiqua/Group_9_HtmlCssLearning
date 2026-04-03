package com.se2.htmlcsslearning.service.strategy.impl;

import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.service.builder.AiGradingPromptBuilder;
import com.se2.htmlcsslearning.dto.GradingContext;
import com.se2.htmlcsslearning.service.strategy.GradingStrategy;

public class CssDebugGradingStrategy implements GradingStrategy {

    @Override
    public String generatePrompt(Challenge challenge, String userCode, GradingContext context) {
        AiGradingPromptBuilder builder = new AiGradingPromptBuilder();

        String systemInstructions = "You are an expert CSS tutor. Focus ONLY on diagnosing CSS syntax and specific property mistakes. \n";

        String evaluationRules = String.format(
                "1. Reference Solution CSS (How the code should look after fix):\n```css\n%s\n```\n" +
                        "2. GRADING INSTRUCTIONS:\n" +
                        "   - Compare the Student Code with the Reference Solution CSS carefully.\n" +
                        "   - Identify syntax errors, property mistakes, or missing logic compared to the reference.\n" +
                        "   - CRITICAL: If the student code's <style> tag or CSS block is empty, or the specific bug defined in the reference is NOT fixed, the score MUST be 0.\n" +
                        "   - Determine the overall correctness and completion percentage (0-100).\n" +
                        "3. OUTPUT RULE: Provide feedback on the specific bugs found (or not found) and a final 'score' in the JSON object.",
                context.getReferenceCss());

        return builder
                .addSystemInstructions(systemInstructions)
                .addEvaluationRules(evaluationRules)
                .addFeedbackTemplate()
                .addUserCode(userCode)
                .addTargetDescriptions("Challenge Title: " + challenge.getChallengeTitle())
                .addOutputFormat()
                .build();
    }
}
