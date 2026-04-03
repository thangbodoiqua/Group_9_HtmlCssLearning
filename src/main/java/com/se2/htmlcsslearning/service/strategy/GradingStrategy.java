package com.se2.htmlcsslearning.service.strategy;

import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.dto.GradingContext;

public interface GradingStrategy {
    String generatePrompt(Challenge challenge, String userCode, GradingContext context);
}
