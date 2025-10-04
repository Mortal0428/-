package com.example.aid.controller;

import com.example.aid.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    // 用户导入页面
    @GetMapping("/import")
    public String importPage() {
        return "admin/user_import";
    }

    // 下载模板
    @GetMapping("/template")
    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/user_import_template.xlsx");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_import_template.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(resource.getInputStream()));
    }

    // 导入用户
    @PostMapping("/import")
    public String importUser(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String failFilePath = adminUserService.importUsers(file);
        if (failFilePath != null) {
            model.addAttribute("failFilePath", failFilePath);
        } else {
            model.addAttribute("success", true);
        }
        return "admin/user_import";
    }

    // 下载导入失败清单
    @GetMapping("/fail")
    public ResponseEntity<InputStreamResource> downloadFail(@RequestParam String path) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/" + path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(resource.getInputStream()));
    }

    // 用户导出
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportUsers() throws IOException {
        return adminUserService.exportUsers();
    }
}



