package com.example.aid.controller;

import com.example.aid.model.Announcement;
import com.example.aid.model.Publicity;
import com.example.aid.service.AnnouncementService;
import com.example.aid.service.PublicityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private PublicityService publicityService;

    // 学生主页
    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        List<Announcement> announcements = announcementService.findAll();
        List<Publicity> publicities = publicityService.findAll();
        model.addAttribute("announcements", announcements);
        model.addAttribute("publicities", publicities);
        model.addAttribute("username", authentication.getName());
        return "student/home";
    }
}



