package com.se2.htmlcsslearning.dto.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailRequest {
    private String to;
    private String subject;
    private String otp;
}