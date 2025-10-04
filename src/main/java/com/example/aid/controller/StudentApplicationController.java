package com.example.aid.controller;

import com.example.aid.model.Application;
import com.example.aid.model.AuditRecord;
import com.example.aid.model.Material;
import com.example.aid.model.User;
import com.example.aid.repository.AuditRecordRepository;
import com.example.aid.service.ApplicationService;
import com.example.aid.service.MaterialService;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/student/application")
public class StudentApplicationController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UserService userService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private AuditRecordRepository auditRecordRepository;
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @GetMapping("/new")
    public String newForm() {
        return "student/application_new";
    }

    @PostMapping("/create")
    public String create(@RequestParam String type, @RequestParam String reason, Authentication authentication, RedirectAttributes redirectAttributes) {
        User student = userService.findByUsername(authentication.getName()).orElse(null);
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "用户信息不存在");
            return "redirect:/student/application/new";
        }
        
        // 检查是否已有同类型的申请（排除已拒绝和已完成的申请）
        List<Application> existingApps = applicationService.findByStudentAndType(student, type);
        boolean hasActiveApplication = existingApps.stream()
            .anyMatch(app -> !"rejected".equals(app.getStatus()) && !"approved".equals(app.getStatus()));
        
        if (hasActiveApplication) {
            String typeName = "grant".equals(type) ? "助学金" : "国家励志奖学金";
            redirectAttributes.addFlashAttribute("error", "您已有一个" + typeName + "申请正在进行中，不能重复申请");
            return "redirect:/student/application/new";
        }
        
        Application app = new Application();
        app.setStudent(student);
        app.setType(type); // grant or scholarship
        app.setStatus("draft");
        app.setReason(reason);
        app.setUpdateTime(LocalDateTime.now());
        applicationService.save(app);
        redirectAttributes.addFlashAttribute("success", "申请创建成功");
        return "redirect:/student/application/progress";
    }

    @PostMapping("/submit/{id}")
    public String submit(@PathVariable Long id) {
        Application app = applicationService.findById(id).orElse(null);
        if (app != null) {
            app.setStatus("submitted");
            app.setSubmitTime(LocalDateTime.now());
            app.setUpdateTime(LocalDateTime.now());
            applicationService.save(app);
        }
        return "redirect:/student/application/progress";
    }

    @PostMapping("/submitToGroup/{id}")
    public String submitToGroup(@PathVariable Long id) {
        Application app = applicationService.findById(id).orElse(null);
        if (app != null) {
            app.setStatus("group_review");
            app.setSubmitTime(LocalDateTime.now());
            app.setUpdateTime(LocalDateTime.now());
            applicationService.save(app);
        }
        return "redirect:/student/application/progress";
    }

    @GetMapping("/progress")
    public String progress(@RequestParam(required = false) Long id, Authentication authentication, Model model) {
        User student = userService.findByUsername(authentication.getName()).orElse(null);
        if (student == null) {
            return "redirect:/login";
        }
        
        // 获取该学生的所有申请
        List<Application> allApplications = applicationService.findByStudent(student);
        model.addAttribute("allApplications", allApplications);
        
        Application selectedApp = null;
        if (id != null) {
            // 如果指定了ID，查找对应的申请
            selectedApp = allApplications.stream()
                .filter(app -> app.getId().equals(id))
                .findFirst()
                .orElse(null);
        } else if (!allApplications.isEmpty()) {
            // 如果没有指定ID，选择最新的申请
            selectedApp = allApplications.stream()
                .max((a1, a2) -> a2.getUpdateTime().compareTo(a1.getUpdateTime()))
                .orElse(null);
        }
        
        model.addAttribute("app", selectedApp);
        if (selectedApp != null) {
            model.addAttribute("materials", materialService.findByApplication(selectedApp));
            // 获取最新的拒绝理由 - 查找所有角色的拒绝记录
            List<AuditRecord> allRecords = auditRecordRepository.findByApplicationOrderByReviewTimeDesc(selectedApp);
            AuditRecord latestRejection = null;
            for (AuditRecord record : allRecords) {
                if ("rejected".equals(record.getResult())) {
                    latestRejection = record;
                    break;
                }
            }
            if (latestRejection != null) {
                model.addAttribute("latestRejection", latestRejection);
            }
            // 添加所有审核记录用于调试
            model.addAttribute("allAuditRecords", allRecords);
        } else {
            // 如果没有选中的申请，提供空列表
            model.addAttribute("materials", new java.util.ArrayList<>());
            model.addAttribute("allAuditRecords", new java.util.ArrayList<>());
        }
        return "student/application_progress_with_sidebar";
    }
    
    @GetMapping("/progress/test")
    public String progressTest(@RequestParam(required = false) Long id, Authentication authentication, Model model) {
        try {
            User student = userService.findByUsername(authentication.getName()).orElse(null);
            if (student == null) {
                return "redirect:/login";
            }
            
            // 获取该学生的所有申请
            List<Application> allApplications = applicationService.findByStudent(student);
            model.addAttribute("allApplications", allApplications);
            
            Application selectedApp = null;
            if (id != null) {
                selectedApp = allApplications.stream()
                    .filter(app -> app.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            } else if (!allApplications.isEmpty()) {
                selectedApp = allApplications.stream()
                    .max((a1, a2) -> a2.getUpdateTime().compareTo(a1.getUpdateTime()))
                    .orElse(null);
            }
            
            model.addAttribute("app", selectedApp);
            // 暂时不加载materials，先测试基本功能
            model.addAttribute("materials", new java.util.ArrayList<>());
            return "student/application_progress_test";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "student/application_progress_test";
        }
    }

    @PostMapping("/material/{id}")
    public String uploadMaterial(@PathVariable Long id, @RequestParam("file") MultipartFile file, @RequestParam(required=false) String description, RedirectAttributes redirectAttributes) {
        try {
            Application app = applicationService.findById(id).orElse(null);
            if (app != null && !file.isEmpty()) {
                // 检查文件大小
                if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                    redirectAttributes.addFlashAttribute("uploadError", "文件大小不能超过10MB");
                    return "redirect:/student/application/progress";
                }
                
                // 创建上传目录 - 使用绝对路径确保目录存在
                File dir = new File(System.getProperty("user.dir") + File.separator + "uploads");
                if (!dir.exists()) {
                    boolean created = dir.mkdirs();
                    if (!created) {
                        redirectAttributes.addFlashAttribute("uploadError", "无法创建上传目录: " + dir.getAbsolutePath());
                        return "redirect:/student/application/progress";
                    }
                }
                
                // 生成安全的文件名
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.trim().isEmpty()) {
                    originalFilename = "uploaded_file";
                }
                // 移除文件名中的特殊字符，避免路径问题
                String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
                String filename = System.currentTimeMillis() + "_" + safeFilename;
                
                File dest = new File(dir, filename);
                file.transferTo(dest);
                
                Material m = new Material();
                m.setApplication(app);
                m.setFilePath("/uploads/" + filename);
                m.setDescription(description);
                materialService.save(m);
                
                redirectAttributes.addFlashAttribute("uploadSuccess", "文件上传成功！");
            } else {
                redirectAttributes.addFlashAttribute("uploadError", "请选择要上传的文件");
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("uploadError", "文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("uploadError", "文件上传失败: " + e.getMessage());
        }
        return "redirect:/student/application/progress";
    }
}