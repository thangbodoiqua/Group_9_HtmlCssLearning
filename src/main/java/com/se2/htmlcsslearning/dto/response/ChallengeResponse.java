package com.se2.htmlcsslearning.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChallengeResponse {
    private boolean success;
    private int score;
    private String feedback;
    private String message;
    private Long submissionId;
}
