package com.website.skateshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/access-denied")
    public String handleAccessDenied() {
        return "access-denied";
    }

    @GetMapping("/error")
    public String handleError() {
        return "error";
    }
}