package com.se2.htmlcsslearning.controller;

import com.se2.htmlcsslearning.service.CssPropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/compare-css")
public class CssPropertyController {
    private final CssPropertyService cssPropertyService;
    private static final String FALL_PATH = "/compare-css/background-color";
    private static final String FILE_PATH = "/compare-css/property/";
    public CssPropertyController(CssPropertyService cssPropertyService) {
        this.cssPropertyService = cssPropertyService;
    }

    @GetMapping
    public String compareCssHome() {
        return "redirect:" + FALL_PATH;
    }

    @GetMapping("/{cssPropertyName}")
    public String getPropertyPage(
            @PathVariable("cssPropertyName") String cssPropertyName,
            Model model
    ) {
        List<String> allPropertyNames = cssPropertyService.getPropertyNameList();
        if(!allPropertyNames.contains(cssPropertyName)){
            return "redirect:" + FALL_PATH;
        }
        Collections.sort(allPropertyNames);
        model.addAttribute("allPropertyNames", allPropertyNames);
        model.addAttribute("selectedPropertyName", cssPropertyName);
        return FILE_PATH + cssPropertyName;
    }
}
