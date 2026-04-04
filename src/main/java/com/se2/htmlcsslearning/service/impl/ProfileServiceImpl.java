package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.dto.request.ChangePasswordRequest;
import com.se2.htmlcsslearning.dto.request.ProfileRequest;
import com.se2.htmlcsslearning.entity.User;
import com.se2.htmlcsslearning.repository.UserRepository;
import com.se2.htmlcsslearning.service.ProfileService;
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

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public ProfileRequest getCurrentProfile() {
        User user = userRepository.findByEmail(getCurrentUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return ProfileRequest.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .dob(user.getDob())
                .build();
    }

    @Transactional
    @Override
    public void updateProfile(ProfileRequest dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserName(dto.getUserName());

        user.setDob(dto.getDob());

        userRepository.save(user);
    }

    @Transactional
    @Override
    public boolean changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(getCurrentUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

}


