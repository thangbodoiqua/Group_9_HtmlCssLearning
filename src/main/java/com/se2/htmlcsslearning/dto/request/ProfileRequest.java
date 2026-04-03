package com.se2.htmlcsslearning.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private Integer id;
    private String userName;
    private String email;
    private String userRole;
    private String dob;
}
