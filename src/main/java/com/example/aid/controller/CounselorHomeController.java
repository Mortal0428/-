package com.example.aid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/counselor")
public class CounselorHomeController {
    @GetMapping("/home")
    public String home() {
        return "counselor/home";
    }
}


