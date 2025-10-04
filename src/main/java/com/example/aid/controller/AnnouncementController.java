package com.example.aid.controller;

import com.example.aid.model.Announcement;
import com.example.aid.model.User;
import com.example.aid.service.AnnouncementService;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private UserService userService;

    // 公告列表
    @GetMapping
    public String list(Model model) {
        List<Announcement> announcements = announcementService.findAll();
        model.addAttribute("announcements", announcements);
        return "announcement/list";
    }

    // 公告详情
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Announcement> announcement = announcementService.findById(id);
        if (announcement.isPresent()) {
            model.addAttribute("announcement", announcement.get());
            return "announcement/detail";
        } else {
            return "redirect:/announcements";
        }
    }

    // 公告发布页面（仅管理员可见）
    @GetMapping("/publish")
    public String publishForm() {
        return "announcement/publish";
    }

    // 公告发布处理
    @PostMapping("/publish")
    public String publish(@RequestParam String title, @RequestParam String content, Authentication authentication) {
        String username = authentication.getName();
        User creator = userService.findByUsername(username).orElse(null);
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setCreator(creator);
        announcementService.save(announcement);
        
        // 记录操作日志
        if (creator != null) {
            // 这里需要注入OperationLogService，暂时注释
            // operationLogService.logOperation(creator, "发布公告", "发布公告: " + title);
        }
        
        return "redirect:/announcements";
    }
}

