package com.se2.htmlcsslearning.repository;

import com.se2.htmlcsslearning.entity.CssProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CssPropertyRepository extends JpaRepository<CssProperty, Long> {
    Optional<CssProperty> findByCssPropertyNameIgnoreCase(String cssPropertyName);

    @Query("SELECT c.cssPropertyName FROM CssProperty c")
    List<String> findAllCssPropertyNames();
}
