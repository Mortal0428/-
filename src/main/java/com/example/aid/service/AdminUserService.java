package com.example.aid.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.example.aid.model.User;
import com.example.aid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 导入用户，返回失败清单文件名（如有失败）
    public String importUsers(MultipartFile file) throws IOException {
        List<UserImportRow> failList = new ArrayList<>();
        List<User> saveList = new ArrayList<>();
        try (InputStream is = file.getInputStream()) {
            List<UserImportRow> rows = EasyExcel.read(is).head(UserImportRow.class).sheet().doReadSync();
            for (UserImportRow row : rows) {
                String reason = checkRow(row);
                if (reason != null) {
                    row.setFailReason(reason);
                    failList.add(row);
                } else {
                    User user = new User();
                    user.setName(row.getName());
                    user.setUsername(row.getUsername());
                    user.setPassword(passwordEncoder.encode(row.getPassword()));
                    user.setRole(row.getRole());
                    user.setStudentNo(row.getStudentNo());
                    user.setStaffNo(row.getStaffNo());
                    user.setClassName(row.getClassName());
                    user.setCollege(row.getCollege());
                    saveList.add(user);
                }
            }
        } catch (ExcelAnalysisException e) {
            throw new IOException("Excel格式错误");
        }
        // 批量保存
        userRepository.saveAll(saveList);
        // 生成失败清单
        if (!failList.isEmpty()) {
            String failFile = "user_import_fail_" + System.currentTimeMillis() + ".xlsx";
            String path = "src/main/resources/static/" + failFile;
            EasyExcel.write(path, UserImportRow.class).sheet("失败清单").doWrite(failList);
            return failFile;
        }
        return null;
    }

    // 校验单行
    private String checkRow(UserImportRow row) {
        if (row.getRole() == null || !(row.getRole().equals("student") || row.getRole().equals("counselor") || row.getRole().equals("dean") || row.getRole().equals("admin"))) {
            return "角色无效";
        }
        if (row.getRole().equals("student")) {
            if (row.getStudentNo() == null || row.getStudentNo().isEmpty()) return "学号不能为空";
            if (userRepository.findByStudentNo(row.getStudentNo()).isPresent()) return "学号已存在";
        }
        if (row.getRole().equals("counselor") || row.getRole().equals("dean") || row.getRole().equals("admin")) {
            if (row.getStaffNo() == null || row.getStaffNo().isEmpty()) return "工号不能为空";
            if (userRepository.findByStaffNo(row.getStaffNo()).isPresent()) return "工号已存在";
        }
        if (userRepository.findByUsername(row.getUsername()).isPresent()) return "用户名已存在";
        return null;
    }

    // 用户导出
    public ResponseEntity<InputStreamResource> exportUsers() throws IOException {
        List<User> users = userRepository.findAll();
        List<UserImportRow> rows = users.stream().map(UserImportRow::fromUser).collect(Collectors.toList());
        String file = "user_export_" + System.currentTimeMillis() + ".xlsx";
        String path = "src/main/resources/static/" + file;
        EasyExcel.write(path, UserImportRow.class).sheet("用户信息").doWrite(rows);
        FileInputStream fis = new FileInputStream(path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fis));
    }

    // 导入/导出用的行对象
    public static class UserImportRow {
        private String name;
        private String username;
        private String password;
        private String role;
        private String studentNo;
        private String staffNo;
        private String className;
        private String college;
        private String failReason;
        // getter/setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getStudentNo() { return studentNo; }
        public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
        public String getStaffNo() { return staffNo; }
        public void setStaffNo(String staffNo) { this.staffNo = staffNo; }
        public String getClassName() { return className; }
        public void setClassName(String className) { this.className = className; }
        public String getCollege() { return college; }
        public void setCollege(String college) { this.college = college; }
        public String getFailReason() { return failReason; }
        public void setFailReason(String failReason) { this.failReason = failReason; }
        public static UserImportRow fromUser(User user) {
            UserImportRow row = new UserImportRow();
            row.setName(user.getName());
            row.setUsername(user.getUsername());
            row.setPassword(""); // 导出不显示密码
            row.setRole(user.getRole());
            row.setStudentNo(user.getStudentNo());
            row.setStaffNo(user.getStaffNo());
            row.setClassName(user.getClassName());
            row.setCollege(user.getCollege());
            return row;
        }
    }
}



