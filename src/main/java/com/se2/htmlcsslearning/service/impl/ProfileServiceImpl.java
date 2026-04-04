package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.dto.request.ChangePasswordRequest;
import com.se2.htmlcsslearning.dto.request.ProfileRequest;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.repository.UserRepository;
import com.se2.htmlcsslearning.service.ProfileService;
import com.se2.htmlcsslearning.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ProfileRequest getCurrentProfile() {
        User curUser = SecurityUtils.getCurrentUser();

        User user = userRepository.findByEmail(curUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return ProfileRequest.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .dob(user.getDob() != null ? user.getDob().toString() : null)
                .build();
    }

    @Transactional
    @Override
    public void updateProfile(ProfileRequest dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(dto.getUserName());

        if (dto.getDob() != null && !dto.getDob().isEmpty()) {
            user.setDob(LocalDate.parse(dto.getDob()));
        }

        userRepository.save(user);

        SecurityUtils.refreshAuthentication(user);
    }

    @Transactional
    @Override
    public boolean changePassword(ChangePasswordRequest request) {
        User curUser = SecurityUtils.getCurrentUser();

        User user = userRepository.findByEmail(curUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }
}


