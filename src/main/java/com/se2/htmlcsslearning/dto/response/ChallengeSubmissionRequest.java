package com.se2.htmlcsslearning.dto.response;

import lombok.Data;

@Data
public class ChallengeSubmissionRequest {
    private String challengeTitle;
    private String userCode;
    private String htmlCode;
    private String cssCode;
    private Double visualScore;
    private String modelId;
}
