package com.example.aid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutTestPageController {
    
    @GetMapping("/test-logout-page")
    public String testLogoutPage() {
        return "test_logout";
    }
}
