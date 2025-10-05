package com.example.aid.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具类
 * 用于生成正确的BCrypt密码哈希
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成123456的BCrypt哈希
        String password = "123456";
        String hashedPassword = encoder.encode(password);
        
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt哈希: " + hashedPassword);
        
        // 验证密码是否正确
        boolean matches = encoder.matches(password, hashedPassword);
        System.out.println("密码验证: " + (matches ? "正确" : "错误"));
        
        // 生成SQL更新语句
        System.out.println("\nSQL更新语句:");
        System.out.println("UPDATE `user` SET `password` = '" + hashedPassword + "' WHERE `id` > 0;");
    }
}
