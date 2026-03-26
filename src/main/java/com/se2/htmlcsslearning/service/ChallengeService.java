package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.controller.dto.ChallengeResponse;
import com.se2.htmlcsslearning.controller.dto.ChallengeSubmissionRequest;

public interface ChallengeService {
    ChallengeResponse processChallengeResponse(ChallengeSubmissionRequest request);
}
