package com.se2.htmlcsslearning.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.service.GradingService;
import com.se2.htmlcsslearning.service.dto.GradingContext;
import com.se2.htmlcsslearning.service.dto.GradingResult;
import com.se2.htmlcsslearning.service.factory.AiModelFactory;
import com.se2.htmlcsslearning.service.factory.GradingStrategyFactory;
import com.se2.htmlcsslearning.service.strategy.GradingStrategy;
import com.se2.htmlcsslearning.service.util.ResourceReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GradingServiceImpl implements GradingService {

    private static final Logger logger = LoggerFactory.getLogger(GradingServiceImpl.class);

    @Autowired
    private GradingStrategyFactory strategyFactory;

    @Autowired
    private AiModelFactory aiModelFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public GradingResult evaluate(Challenge challenge, String userCode, String modelId, Double visualScore) {
        String type = challenge.getChallengeType().getChallengeType();
        String folderType = type.equals("CSS_DEBUG") ? "css-debug" : "pixel-perfect";
        String challengeName = challenge.getChallengeTitle();

        GradingContext context = new GradingContext();
        context.setVisualScore(visualScore);
        try {
            context.setHtmlTemplate(ResourceReaderUtil.readFile(folderType, challengeName, "index.html"));
            context.setReferenceCss(ResourceReaderUtil.readFile(folderType, challengeName, "solution.css"));
        } catch (Exception e) {
            logger.error("Failed to read resources for challenge: " + challengeName, e);
            throw new RuntimeException("Failed to read necessary resources for evaluation.", e);
        }

        GradingStrategy strategy = strategyFactory.getStrategy(type);

        String promptText = strategy.generatePrompt(challenge, userCode, context);

        logger.info("Sending prompt to AI for challenge: {}", challenge.getChallengeTitle());
        logger.debug("Prompt Text: {}", promptText);

        ChatModel chatModel = aiModelFactory.getModel(modelId);

        String aiResponse = chatModel.call(promptText);
        logger.info("Raw AI Response received.");
        logger.debug("Raw AI Response Content: {}", aiResponse);

        try {
            String cleanedResponse = aiResponse.replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```\\s*", "")
                    .replaceAll("(?s)^.*?(\\{)", "$1")
                    .replaceAll("(?s)(\\}).*?$", "$1")
                    .trim();

            logger.info("Cleaned JSON for parsing: {}", cleanedResponse);

            @SuppressWarnings("unchecked")
            Map<String, Object> result = objectMapper.copy()
                    .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
                    .readValue(cleanedResponse, Map.class);

            Object scoreObj = result.get("score");
            int score = (scoreObj instanceof Number) ? ((Number) scoreObj).intValue() : 0;
            String feedback = (String) result.getOrDefault("feedback", "No feedback provided.");

            return GradingResult.builder()
                    .score(score)
                    .feedback(feedback)
                    .build();

        } catch (Exception e) {
            logger.error("Failed to process AI response: ", e);
            throw new RuntimeException("AI Evaluation failed: " + e.getMessage());
        }
    }
}
