package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.dto.request.MailRequest;

public interface MailService {
    public void sendEmail(MailRequest request);
}
