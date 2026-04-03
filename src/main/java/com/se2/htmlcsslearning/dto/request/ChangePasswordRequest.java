package com.se2.htmlcsslearning.dto.request;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
