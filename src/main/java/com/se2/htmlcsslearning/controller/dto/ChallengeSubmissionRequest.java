package com.se2.htmlcsslearning.controller.dto;

import lombok.Data;

@Data
public class ChallengeSubmissionRequest {
    private Long challengeId;
    private String userCode;
    private String modelId;
}
