package com.example.aid.controller;

import com.example.aid.model.Application;
import com.example.aid.model.Material;
import com.example.aid.model.User;
import com.example.aid.service.ApplicationService;
import com.example.aid.service.MaterialService;
import com.example.aid.service.ReviewService;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/group/review")
public class GroupReviewController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public String list(Model model) {
        List<Application> pending = applicationService.findByStatus("group_review");
        model.addAttribute("apps", pending);
        return "review/group_list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Application app = applicationService.findById(id).orElse(null);
        if (app == null) return "redirect:/group/review";
        List<Material> materials = materialService.findByApplication(app);
        model.addAttribute("app", app);
        model.addAttribute("materials", materials);
        return "review/group_detail";
    }

    @PostMapping("/{id}")
    public String decide(@PathVariable Long id, @RequestParam String result, @RequestParam String reason, Authentication authentication) {
        Application app = applicationService.findById(id).orElse(null);
        User reviewer = userService.findByUsername(authentication.getName()).orElse(null);
        if (app != null && reviewer != null) {
            // 小组审核完成后，流转到辅导员复审
            String next = "counselor_review"; // 通过后进入辅导员复审
            reviewService.review(app, reviewer, "group", result, reason, next);
        }
        return "redirect:/group/review";
    }
}
