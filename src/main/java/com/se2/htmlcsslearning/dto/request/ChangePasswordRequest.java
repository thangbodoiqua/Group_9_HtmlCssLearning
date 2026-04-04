package com.se2.htmlcsslearning.dto.request;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    public interface Step1 {}
    public interface Step2 {}
    public interface Step3 {}
    @GroupSequence({Step1.class, Step2.class, Step3.class})
    public interface ValidationOrder {}

    @NotBlank(message = "Current password is required", groups = Step1.class)
    private String currentPassword;
    @NotBlank(message = "New password is required", groups = Step2.class)
    @Size(min = 6, message = "New password must be at least 6 characters", groups = Step2.class)

    private String newPassword;

    @NotBlank(message = "Please confirm your new password", groups = Step3.class)
    private String confirmPassword;
    @AssertTrue(message = "Passwords do not match", groups = Step3.class)
    public boolean isPasswordMatching() {
        if (newPassword == null || confirmPassword == null) return true;
        return newPassword.equals(confirmPassword);
    }
}
