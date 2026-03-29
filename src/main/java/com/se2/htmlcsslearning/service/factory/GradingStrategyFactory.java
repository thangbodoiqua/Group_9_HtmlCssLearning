package com.se2.htmlcsslearning.service.factory;

import com.se2.htmlcsslearning.service.strategy.GradingStrategy;
import com.se2.htmlcsslearning.service.strategy.impl.CssDebugGradingStrategy;
import com.se2.htmlcsslearning.service.strategy.impl.PixelPerfectGradingStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GradingStrategyFactory {
    private final Map<String, GradingStrategy> strategies = new HashMap<>();

    public GradingStrategyFactory() {
        strategies.put("CSS_DEBUG", new CssDebugGradingStrategy());
        strategies.put("PIXEL_PERFECT", new PixelPerfectGradingStrategy());
    }

    public GradingStrategy getStrategy(String type) {
        GradingStrategy strategy = strategies.get(type.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown challenge type: " + type);
        }
        return strategy;
    }
}
