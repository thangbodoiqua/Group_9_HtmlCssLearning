package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.dto.request.MailRequest;
import com.se2.htmlcsslearning.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(MailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Markuply");
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText("Hello,\n\nYour OTP is: " + request.getOtp() +
                "\nThis OTP is valid for 5 minutes. Please do not share this OTP with anyone.");
        mailSender.send(message);
    }

}
