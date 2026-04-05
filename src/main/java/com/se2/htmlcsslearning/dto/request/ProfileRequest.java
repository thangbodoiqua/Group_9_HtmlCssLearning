package com.se2.htmlcsslearning.dto.request;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    public interface Step1 {}
    public interface Step2 {}
    public interface Step3 {}

    @GroupSequence({Step1.class, Step2.class, Step3.class})
    public interface ValidationOrder {}

    private Integer id;
    @NotBlank(message = "Username is required", groups = Step1.class)
    @Pattern(regexp = "^[a-zA-Z\\sÀ-ỹ]+$", message = "Username must be letters, not numbers", groups = Step1.class)
    @Size(max = 24 , min = 6, message = "Username must less than 24 characters and more than 6 characters",  groups = Step1.class)
    private String userName;

    @NotBlank(message = "Email is required", groups = Step2.class)
    @Email(message = "Invalid email format" ,groups = Step2.class)
    private String email;

    private String userRole;

    @NotNull(message = "Date of birth is required", groups = Step3.class)
    @Past(message = "Date of birth must be in the past", groups = Step3.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
}
