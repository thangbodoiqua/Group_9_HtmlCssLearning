package com.se2.htmlcsslearning.dto.request;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
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

    @GroupSequence({Step1.class, Step2.class})
    public interface ValidationOrder {}
    private Integer id;
    @NotBlank(message = "Username is required", groups = Step1.class)
    @Pattern(regexp = "^[a-zA-Z\\sÀ-ỹ]+$", message = "Username must be letters, not numbers", groups = Step1.class)
    private String userName;
    private String email;
    private String userRole;
    @NotNull(message = "Date of birth is required", groups = Step2.class)
    @Past(message = "Date of birth must be in the past", groups = Step2.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
}
