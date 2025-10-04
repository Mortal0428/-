package com.example.aid.controller;

import com.example.aid.model.Application;
import com.example.aid.model.User;
import com.example.aid.service.ApplicationService;
import com.example.aid.service.ReviewService;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/counselor/review")
public class CounselorReviewController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String list(Model model, Authentication authentication) {
        User counselor = userService.findByUsername(authentication.getName()).orElse(null);
        // 简化：取所有submitted作为待辅导员审核
        List<Application> pending = new ArrayList<>(applicationService.findByStatus("submitted"));
        model.addAttribute("apps", pending);
        return "review/counselor_list";
    }

    @PostMapping("/{id}")
    public String decide(@PathVariable Long id, @RequestParam String result, @RequestParam String reason, Authentication authentication) {
        Application app = applicationService.findById(id).orElse(null);
        User counselor = userService.findByUsername(authentication.getName()).orElse(null);
        if (app != null && counselor != null) {
            reviewService.review(app, counselor, "counselor", result, reason, "group_review");
        }
        return "redirect:/counselor/review";
    }
}


