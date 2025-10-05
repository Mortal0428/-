package com.example.aid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestAllPortalsController {
    
    @GetMapping("/test-all-portals")
    public String testAllPortals() {
        return "test_all_portals";
    }
}
