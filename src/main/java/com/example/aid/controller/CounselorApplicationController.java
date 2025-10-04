package com.example.aid.controller;

import com.example.aid.model.Application;
import com.example.aid.model.AuditRecord;
import com.example.aid.model.Material;
import com.example.aid.model.User;
import com.example.aid.repository.AuditRecordRepository;
import com.example.aid.service.ApplicationService;
import com.example.aid.service.MaterialService;
import com.example.aid.service.ReviewService;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/counselor/applications")
public class CounselorApplicationController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AuditRecordRepository auditRecordRepository;
    @Autowired
    private MaterialService materialService;

    @GetMapping
    public String list(@RequestParam(required=false) String clazz,
                       @RequestParam(required=false) String type,
                       @RequestParam(required=false) String state,
                       Authentication authentication,
                       Model model) {
        User counselor = userService.findByUsername(authentication.getName()).orElse(null);
        List<Application> result = new ArrayList<>();
        String[] states = state == null || state.isEmpty() ? new String[]{"submitted","counselor_review","dean_review","approved","rejected"} : new String[]{state};
        Map<Long, String> groupOpinion = new HashMap<>();
        for (String s : states) {
            for (Application a : applicationService.findByStatus(s)) {
                if (a.getStudent() == null) continue;
                if (clazz != null && !clazz.isEmpty() && !clazz.equals(a.getStudent().getClassName())) continue;
                if (type != null && !type.isEmpty() && !type.equals(a.getType())) continue;
                result.add(a);
                // 取最近一次评议小组意见
                for (AuditRecord ar : auditRecordRepository.findByApplicationOrderByReviewTimeDesc(a)) {
                    if ("group".equals(ar.getRole())) { groupOpinion.put(a.getId(), ar.getReason()); break; }
                }
            }
        }
        model.addAttribute("apps", result);
        model.addAttribute("groupOpinion", groupOpinion);
        model.addAttribute("clazz", clazz == null ? "" : clazz);
        model.addAttribute("type", type == null ? "" : type);
        model.addAttribute("state", state == null ? "" : state);
        return "counselor/app_list";
    }

    // 查看申请详情
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Application app = applicationService.findById(id).orElse(null);
        if (app == null) return "redirect:/counselor/applications";
        
        List<Material> materials = materialService.findByApplication(app);
        List<AuditRecord> auditRecords = auditRecordRepository.findByApplicationOrderByReviewTimeDesc(app);
        
        model.addAttribute("app", app);
        model.addAttribute("materials", materials);
        model.addAttribute("auditRecords", auditRecords);
        return "counselor/app_detail";
    }

    // 小组通过后，辅导员复审提交到院长
    @PostMapping("/submitToDean")
    public String submitToDean(@RequestParam Long id, @RequestParam String reason, Authentication authentication) {
        Application app = applicationService.findById(id).orElse(null);
        User counselor = userService.findByUsername(authentication.getName()).orElse(null);
        if (app != null && "counselor_review".equals(app.getStatus())) {
            reviewService.review(app, counselor, "counselor", "approved", reason, "dean_review");
        }
        return "redirect:/counselor/applications?state=counselor_review";
    }
}