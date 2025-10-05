package com.example.aid.controller;

import com.example.aid.model.User;
import com.example.aid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class PasswordFixController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @GetMapping("/fix-passwords")
    @ResponseBody
    public String fixPasswords() {
        try {
            // 生成正确的BCrypt哈希
            String newPassword = "123456";
            String hashedPassword = passwordEncoder.encode(newPassword);
            
            // 更新所有用户密码
            List<User> users = userRepository.findAll();
            for (User user : users) {
                user.setPassword(hashedPassword);
                userRepository.save(user);
            }
            
            return "密码修复成功！所有用户密码已设置为123456<br>" +
                   "生成的BCrypt哈希: " + hashedPassword + "<br>" +
                   "哈希长度: " + hashedPassword.length() + "<br>" +
                   "更新用户数量: " + users.size();
                   
        } catch (Exception e) {
            return "密码修复失败: " + e.getMessage();
        }
    }
}
