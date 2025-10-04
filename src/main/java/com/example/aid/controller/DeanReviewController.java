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
import java.util.List;

@Controller
@RequestMapping("/dean/review")
public class DeanReviewController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String list(Model model) {
        List<Application> pending = applicationService.findByStatus("dean_review");
        model.addAttribute("apps", pending);
        return "review/dean_list";
    }

    @PostMapping("/{id}")
    public String decide(@PathVariable Long id, @RequestParam String result, @RequestParam String reason, Authentication authentication) {
        Application app = applicationService.findById(id).orElse(null);
        User reviewer = userService.findByUsername(authentication.getName()).orElse(null);
        if (app != null && reviewer != null) {
            // 院长终审：通过进入公示，拒绝直接结束
            String nextStatus = "approved".equals(result) ? "publicity" : "rejected";
            reviewService.review(app, reviewer, "dean", result, reason, nextStatus);
        }
        return "redirect:/dean/review";
    }
}
