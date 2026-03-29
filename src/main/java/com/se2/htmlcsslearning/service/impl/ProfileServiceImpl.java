package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.service.ProfileService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Override
    public User getCurrentProfile() {
        User sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setEmail("student.demo@se2.local");
        sampleUser.setUserRole("Learner");
        sampleUser.setUserName("Demo Student");
        return sampleUser;
    }
}
