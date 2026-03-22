package com.se2.htmlcsslearning.service.impl;

import com.se2.htmlcsslearning.entity.CssProperty;
import com.se2.htmlcsslearning.repository.CssPropertyRepository;
import com.se2.htmlcsslearning.service.CssPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CssPropertyServiceImpl implements CssPropertyService {
    private final CssPropertyRepository cssPropertyRepository;

    public CssPropertyServiceImpl(CssPropertyRepository cssPropertyRepository) {
        this.cssPropertyRepository = cssPropertyRepository;
    }

    @Override
    public List<String> getPropertyNameList() {
        List<String> names = cssPropertyRepository.findAllCssPropertyNames();
        System.out.println(names.toString());
        return names == null ? List.of() :
                names.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
    }


}

