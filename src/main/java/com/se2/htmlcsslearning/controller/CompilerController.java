package com.se2.htmlcsslearning.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

@Controller
public class CompilerController {

    private static final Logger logger = Logger.getLogger(CompilerController.class.getName());
    private static final String DEFAULT_CODE_PATH = "compiler/default-code.html";

    private String getDefaultCode() {
        try {
            ClassPathResource resource = new ClassPathResource(DEFAULT_CODE_PATH);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.severe("Could not read default code file: " + e.getMessage());
            return "<!DOCTYPE html><html><body><h1>Error loading default code</h1></body></html>";
        }
    }

    @GetMapping("/compiler")
    public String showCompiler(@RequestParam(value = "code", required = false) String encodedCode, Model model) {
        String decodedCode = getDefaultCode();

        if (encodedCode != null && !encodedCode.isEmpty()) {
            try {
                decodedCode = new String(Base64.getDecoder().decode(encodedCode));
            } catch (IllegalArgumentException e) {
                decodedCode = encodedCode;
            }
        }

        model.addAttribute("code", decodedCode);
        return "compiler/compiler";
    }

    @PostMapping("/compiler")
    public String handlePostCompiler(@RequestParam("code") String code, Model model) {
        model.addAttribute("code", code);
        return "compiler/compiler";
    }
}
