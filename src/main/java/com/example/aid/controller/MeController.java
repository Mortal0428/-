package com.example.aid.controller;

import com.example.aid.model.*;
import com.example.aid.repository.BindRequestRepository;
import com.example.aid.service.ApplicationService;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class MeController {
    @Autowired
    private BindRequestRepository bindRequestRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationService applicationService;

    // 个人信息与绑定页面
    @GetMapping("/student/profile")
    public String profile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElse(null);
        model.addAttribute("user", user);
        // 最近一次绑定状态
        if (user != null) {
            bindRequestRepository.findTopByStudentOrderByRequestTimeDesc(user).ifPresent(b -> model.addAttribute("lastBind", b));
        }
        return "student/profile";
    }

    // 提交绑定申请
    @PostMapping("/student/bind")
    public String submitBind(@RequestParam String className,
                             @RequestParam String college,
                             @RequestParam String counselorStaffNo,
                             Authentication authentication,
                             Model model) {
        User student = userService.findByUsername(authentication.getName()).orElse(null);
        if (student == null) return "redirect:/login";
        student.setClassName(className);
        student.setCollege(college);
        // 保存学生信息更新
        userService.save(student);
        // 查找辅导员 - 先通过工号查找
        Optional<User> counselorOpt = userService.findByStaffNo(counselorStaffNo);
        // 如果工号找不到，再尝试用户名
        if (!counselorOpt.isPresent()) {
            counselorOpt = userService.findByUsername(counselorStaffNo);
        }
        
        // 仅当确为辅导员才创建绑定申请
        if (counselorOpt.isPresent() && "counselor".equals(counselorOpt.get().getRole())) {
            BindRequest br = new BindRequest();
            br.setStudent(student);
            br.setCounselor(counselorOpt.get());
            br.setStatus("pending");
            br.setRequestTime(LocalDateTime.now());
            bindRequestRepository.save(br);
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", "辅导员工号/账号不存在或不是辅导员");
        }
        model.addAttribute("user", student);
        bindRequestRepository.findTopByStudentOrderByRequestTimeDesc(student).ifPresent(b -> model.addAttribute("lastBind", b));
        return "student/profile";
    }

    // 学生提交申请至评议小组初审
    @PostMapping("/student/application/submitToGroup")
    public String submitToGroup(@RequestParam Long id, Authentication authentication) {
        Application app = applicationService.findById(id).orElse(null);
        if (app != null) {
            app.setStatus("group_review");
            app.setSubmitTime(LocalDateTime.now());
            app.setUpdateTime(LocalDateTime.now());
            applicationService.save(app);
        }
        return "redirect:/student/application/progress";
    }
}

