package com.se2.htmlcsslearning.service.strategy.impl;

import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.service.builder.AiGradingPromptBuilder;
import com.se2.htmlcsslearning.service.dto.GradingContext;
import com.se2.htmlcsslearning.service.strategy.GradingStrategy;

public class PixelPerfectGradingStrategy implements GradingStrategy {

    @Override
    public String generatePrompt(Challenge challenge, String userCode, GradingContext context) {
        AiGradingPromptBuilder builder = new AiGradingPromptBuilder();
        
        String systemInstructions = "You are an expert Frontend Developer focusing on Pixel Perfect UI development. You will strictly evaluate visual accuracy.";
        
        String evaluationRules = String.format(
                "1. Compare user layout with Reference Code strictly considering positioning, sizes, margins, paddings, and colors.\n" +
                "2. HTML code provided:\n```html\n%s\n```\n" +
                "3. Reference Answer:\n```css\n%s\n```\n" +
                "4. Identify any layout mismatches and provide a score from 0-100.", 
                context.getHtmlTemplate(), context.getReferenceCss()
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
