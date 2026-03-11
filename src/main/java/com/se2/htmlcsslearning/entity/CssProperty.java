package com.se2.htmlcsslearning.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CssProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "css_property_name")
    private String cssPropertyName;
}
