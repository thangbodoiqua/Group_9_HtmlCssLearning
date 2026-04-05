package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationCode, Integer> {
    VerificationCode findByEmailAndOtp(String email, String otp);

    VerificationCode findByEmail(String email);
}