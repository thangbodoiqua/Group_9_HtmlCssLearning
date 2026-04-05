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
        prompt.append("=== STUDENT SUBMITTED CODE STARTS ===\n")
                .append(code)
                .append("\n=== STUDENT SUBMITTED CODE ENDS ===\n\n");
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

    public AiGradingPromptBuilder addFeedbackTemplate() {
        prompt.append("FEEDBACK STRUCTURE RULES:\n")
                .append("Your 'feedback' must follow this exact sectioned format:\n")
                .append("1. OVERVIEW: Brief summary of the attempt.\n")
                .append("2. WHAT YOU DID WELL: Bullet points of correctly implemented items.\n")
                .append("3. REMAINING DISCREPANCIES: Bullet points of missing or incorrect CSS rules.\n")
                .append("4. ACTION PLAN: Clear steps to achieve 100% score.\n\n")
                .append("CRITICAL: Do NOT use nested double quotes inside the feedback string. Use single quotes or simple text.\n\n");
        return this;
    }

    public AiGradingPromptBuilder addOutputFormat() {
        prompt.append("CRITICAL JSON RULES:\n")
                .append("1. Return ONLY a single, valid JSON object.\n")
                .append("2. Do NOT use markdown code blocks (like ```json).\n")
                .append("3. All keys and string values MUST be enclosed in double quotes.\n")
                .append("4. IMPORTANT: Inside the \"feedback\" string, use ONLY single quotes (') for highlighting, NEVER double quotes (\").\n")
                .append("5. Ensure all newlines in the feedback are escaped as '\\n'.\n")
                .append("Format: {\"score\": 85, \"feedback\": \"OVERVIEW: ...\\n\\nWHAT YOU DID WELL: ...\\n\\nREMAINING DISCREPANCIES: ...\\n\\nACTION PLAN: ...\" }");
        return this;
    }

    // reset prompt after build
    public String build() {
        String result = prompt.toString();
        prompt.setLength(0);
        return result;
    }
}
