package com.example.aid.controller;

import com.example.aid.model.Application;
import com.example.aid.model.OperationLog;
import com.example.aid.model.User;
import com.example.aid.service.ApplicationService;
import com.example.aid.service.OperationLogService;
import com.example.aid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private UserService userService;

    // 管理员首页 - 显示待公示的申请
    @GetMapping
    public String home(Model model) {
        List<Application> publicityApps = applicationService.findByStatus("publicity");
        model.addAttribute("publicityApps", publicityApps);
        return "admin/home";
    }

    // 确认公示完成，正式通过
    @PostMapping("/confirmPublicity")
    public String confirmPublicity(@RequestParam Long id, Authentication authentication) {
        Application app = applicationService.findById(id).orElse(null);
        if (app != null) {
            app.setStatus("approved");
            app.setUpdateTime(LocalDateTime.now());
            applicationService.save(app);
            
            // 记录操作日志
            User admin = userService.findByUsername(authentication.getName()).orElse(null);
            if (admin != null) {
                operationLogService.logOperation(admin, "确认公示", 
                    "确认学生 " + app.getStudent().getName() + " 的申请公示完成，正式通过");
            }
        }
        return "redirect:/admin";
    }

    // 统计页面
    @GetMapping("/stats")
    public String stats(Model model) {
        List<Application> approved = applicationService.findByStatus("approved");
        List<Application> rejected = applicationService.findByStatus("rejected");
        model.addAttribute("approvedCount", approved.size());
        model.addAttribute("rejectedCount", rejected.size());
        model.addAttribute("approvedApps", approved);
        model.addAttribute("rejectedApps", rejected);
        return "admin/stats";
    }
    
    // 操作日志页面
    @GetMapping("/logs")
    public String logs(@RequestParam(required = false) String role,
                       @RequestParam(required = false) String operationType,
                       @RequestParam(required = false) String startDate,
                       @RequestParam(required = false) String endDate,
                       Model model) {
        List<OperationLog> logs;
        
        if (role != null && !role.isEmpty() && operationType != null && !operationType.isEmpty()) {
            // 按角色和操作类型查询
            logs = operationLogService.findByRole(role);
            logs = logs.stream()
                .filter(log -> log.getOperationType().equals(operationType))
                .toList();
        } else if (role != null && !role.isEmpty()) {
            // 按角色查询
            logs = operationLogService.findByRole(role);
        } else if (operationType != null && !operationType.isEmpty()) {
            // 按操作类型查询
            logs = operationLogService.findByOperationType(operationType);
        } else if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            // 按时间范围查询
            LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            logs = operationLogService.findByTimeRange(start, end);
        } else {
            // 查询所有
            logs = operationLogService.findAll();
        }
        
        model.addAttribute("logs", logs);
        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedOperationType", operationType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        // 统计信息
        model.addAttribute("roleStats", operationLogService.countByRole());
        model.addAttribute("operationStats", operationLogService.countByOperationType());
        
        return "admin/logs";
    }
    
    // 导出操作日志
    @GetMapping("/logs/export")
    public String exportLogs(@RequestParam(required = false) String role,
                            @RequestParam(required = false) String operationType,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate,
                            Model model) {
        // 这里可以实现Excel导出功能，暂时返回日志页面
        return "redirect:/admin/logs?role=" + (role != null ? role : "") + 
               "&operationType=" + (operationType != null ? operationType : "") +
               "&startDate=" + (startDate != null ? startDate : "") +
               "&endDate=" + (endDate != null ? endDate : "");
    }
}
