package com.se2.htmlcsslearning.service;

import com.se2.htmlcsslearning.dto.request.ChangePasswordRequest;
import com.se2.htmlcsslearning.dto.request.ProfileRequest;
import com.se2.htmlcsslearning.entity.User;
import jakarta.transaction.Transactional;

public interface ProfileService {
    ProfileRequest getCurrentProfile();

    void updateProfile(ProfileRequest dto);

    boolean changePassword(ChangePasswordRequest request);
}
