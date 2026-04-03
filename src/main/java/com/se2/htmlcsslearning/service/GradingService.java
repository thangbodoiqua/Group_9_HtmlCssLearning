package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.entity.Challenge;
import com.se2.htmlcsslearning.dto.GradingResult;

public interface GradingService {
    GradingResult evaluate(Challenge challenge, String userCode, String modelId);
}
