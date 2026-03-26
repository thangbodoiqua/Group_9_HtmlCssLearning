package com.se2.htmlcsslearning.service.factory;

import com.se2.htmlcsslearning.service.strategy.CssDebugStrategy;
import com.se2.htmlcsslearning.service.strategy.GradingStrategy;
import com.se2.htmlcsslearning.service.strategy.PixelPerfectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradingStrategyFactory {
    @Autowired
    private PixelPerfectStrategy pixelPerfectStrategy;
    
    @Autowired
    private CssDebugStrategy cssDebugStrategy;

    public GradingStrategy getStrategy(String type) {
        if ("PIXEL_PERFECT".equalsIgnoreCase(type)) {
            return pixelPerfectStrategy;
        } else if ("CSS_DEBUG".equalsIgnoreCase(type)) {
            return cssDebugStrategy;
        }
        throw new IllegalArgumentException("Unknown challenge type: " + type);
    }
}
