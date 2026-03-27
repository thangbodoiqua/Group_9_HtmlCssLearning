package com.se2.htmlcsslearning.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ErrorPageController {

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(HttpServletRequest request, Model model) {
        model.addAttribute("statusCode", 404);
        model.addAttribute("requestUri", request.getRequestURI());
        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage", "The page you're looking for doesn't exist or has been moved.");
        return "error/404";
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handle403(HttpServletRequest request, Model model) {
        model.addAttribute("statusCode", 403);
        model.addAttribute("requestUri", request.getRequestURI());
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage", "You don't have permission to access this page.");
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle500(HttpServletRequest request, Exception ex, Model model) {
        model.addAttribute("statusCode", 500);
        model.addAttribute("requestUri", request.getRequestURI());
        model.addAttribute("errorTitle", "Server Error");
        model.addAttribute("errorMessage", "Something went wrong on our end. Please try again later.");
        return "error/500";
    }
}
