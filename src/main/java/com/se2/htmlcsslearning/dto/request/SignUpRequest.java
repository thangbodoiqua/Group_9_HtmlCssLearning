package com.se2.htmlcsslearning.dto.request;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SignUpRequest {
    public interface Step1 {}
    public interface Step2 {}
    public interface Step3 {}
    public interface Step4 {}
    public interface Step5 {}

    @GroupSequence({Step1.class, Step2.class, Step3.class, Step4.class, Step5.class})
    public interface ValidationOrder {}

    @NotBlank(message = "Full name is required", groups = Step1.class)
    @Size(max = 24 , min = 6, message = "Username must less than 24 characters and more than 6 characters", groups = Step1.class)
    private String name;

    @NotBlank(message = "Email is required", groups = Step2.class)
    @Email(message = "Invalid email format" ,groups = Step2.class)
    private String email;

    @NotBlank(message = "Password is required", groups = Step3.class)
    @Size(min = 6, message = "Password must be at least 6 characters", groups = Step3.class)
    private String password;

    @NotBlank(message = "Please confirm your password", groups = Step4.class)
    private String confirmPassword;

    @NotNull(message = "Date of birth is required" ,groups = Step5.class)
    @Past(message = "Date of birth must be in the past", groups = Step5.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
}