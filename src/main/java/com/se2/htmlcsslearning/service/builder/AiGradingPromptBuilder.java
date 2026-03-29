package com.se2.htmlcsslearning.service.builder;

public class AiGradingPromptBuilder {
    private StringBuilder prompt = new StringBuilder();

    public AiGradingPromptBuilder addSystemInstructions(String instructions) {
        prompt.append("SYSTEM INSTRUCTION: ").append(instructions).append("\n\n");
        return this;
    }

    public AiGradingPromptBuilder addEvaluationRules(String rules) {
        prompt.append("EVALUATION RULES: ").append(rules).append("\n\n");
        return this;
    }

    public AiGradingPromptBuilder addUserCode(String code) {
        prompt.append("STUDENT CODE:\n").append(code).append("\n\n");
        return this;
    }

    public AiGradingPromptBuilder addReferenceCode(String code) {
        prompt.append("REFERENCE SOLUTION CODE (The Ideal Target):\n").append(code).append("\n\n");
        return this;
    }

    public AiGradingPromptBuilder addTargetDescriptions(String info) {
        prompt.append("TARGET DESCRIPTION: ").append(info).append("\n\n");
        return this;
    }

    public AiGradingPromptBuilder addOutputFormat() {
        prompt.append(
                "CRITICAL: Return ONLY a valid JSON object. Do NOT include any markdown formatting (like ```json). \n")
                .append("All keys MUST be enclosed in double quotes. \n")
                .append("Format: {\"score\": 85, \"feedback\": \"Detailed text here\"}");
        return this;
    }

    // reset prompt after build
    public String build() {
        String result = prompt.toString();
        prompt.setLength(0);
        return result;
    }
}
