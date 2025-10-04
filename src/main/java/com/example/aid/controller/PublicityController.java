package com.example.aid.controller;

import com.example.aid.model.Application;
import com.example.aid.model.Publicity;
import com.example.aid.model.User;
import com.example.aid.service.ApplicationService;
import com.example.aid.service.PublicityService;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/publicity")
public class PublicityController {
    @Autowired
    private PublicityService publicityService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;

    // 公示列表
    @GetMapping
    public String list(Model model) {
        List<Publicity> publicities = publicityService.findAll();
        model.addAttribute("publicities", publicities);
        return "publicity/list";
    }

    // 公示详情
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Publicity> publicity = publicityService.findById(id);
        if (publicity.isPresent()) {
            model.addAttribute("publicity", publicity.get());
            return "publicity/detail";
        } else {
            return "redirect:/publicity";
        }
    }

    // 管理员发布公示页面（选择院长审核通过的申请）
    @GetMapping("/publish")
    public String publishForm(Model model) {
        List<Application> approvedApps = applicationService.findByStatus("approved");
        model.addAttribute("applications", approvedApps);
        return "publicity/publish";
    }

    // 管理员发布公示处理
    @PostMapping("/publish")
    public String publish(@RequestParam Long applicationId, Authentication authentication) {
        Application app = applicationService.findById(applicationId).orElse(null);
        if (app != null) {
            Publicity publicity = new Publicity();
            publicity.setApplication(app);
            publicity.setStartTime(LocalDateTime.now());
            publicity.setEndTime(LocalDateTime.now().plusDays(7));
            publicity.setStatus("publicizing");
            publicityService.save(publicity);
            
            // 更新申请状态为公示中
            app.setStatus("publicity");
            app.setUpdateTime(LocalDateTime.now());
            applicationService.save(app);
            
            // 记录操作日志
            User admin = userService.findByUsername(authentication.getName()).orElse(null);
            if (admin != null) {
                // 这里需要注入OperationLogService，暂时注释
                // operationLogService.logOperation(admin, "发布公示", 
                //     "发布学生 " + app.getStudent().getName() + " 的申请公示");
            }
        }
        return "redirect:/publicity";
    }

    // 用户提交异议
    @PostMapping("/objection")
    public String objection(@RequestParam Long publicityId, @RequestParam String objectionContent, Authentication authentication) {
        Publicity publicity = publicityService.findById(publicityId).orElse(null);
        if (publicity != null && publicity.getStatus().equals("publicizing")) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            publicity.setObjectionContent(objectionContent);
            publicity.setObjectionUser(user);
            publicity.setObjectionTime(LocalDateTime.now());
            publicity.setStatus("has_objection");
            publicityService.save(publicity);
        }
        return "redirect:/publicity/" + publicityId;
    }
}

