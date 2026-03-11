package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.CssProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CssPropertyRepository extends JpaRepository<CssProperty, Long> {
}

