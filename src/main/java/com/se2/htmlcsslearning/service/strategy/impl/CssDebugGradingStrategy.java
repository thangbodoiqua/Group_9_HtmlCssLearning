package com.se2.htmlcsslearning.service.strategy.impl;

import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.service.builder.AiGradingPromptBuilder;
import com.se2.htmlcsslearning.service.dto.GradingContext;
import com.se2.htmlcsslearning.service.strategy.GradingStrategy;

public class CssDebugGradingStrategy implements GradingStrategy {

    @Override
    public String generatePrompt(Challenge challenge, String userCode, GradingContext context) {
        AiGradingPromptBuilder builder = new AiGradingPromptBuilder();
        
        String systemInstructions = "You are an expert CSS tutor. Focus ONLY on diagnosing CSS syntax and specific property mistakes. \n" +
                "IMPORTANT RULE: The user is NOT allowed to modify the HTML structure. If they modified the HTML from the original layout (other than whitespace differences), you must score them 0 and warn them.";
        
        String evaluationRules = String.format(
                "1. Check if the student modified the ORIGINAL HTML.\n" +
                "2. The original HTML structure provided is:\n```html\n%s\n```\n" +
                "3. Reference CSS Answer:\n```css\n%s\n```\n" +
                "4. Compare the Student Code with the Reference CSS and ORIGINAL HTML. If the student modified the HTML, score=0. Otherwise, identify the CSS bug, give hints, and a score from 0-100.", 
                context.getHtmlTemplate(), context.getReferenceCss()
        );

        return builder
                .addSystemInstructions(systemInstructions)
                .addEvaluationRules(evaluationRules)
                .addUserCode(userCode)
                .addTargetDescriptions("Challenge Title: " + challenge.getChallengeTitle())
                .addOutputFormat()
                .build();
    }
}
