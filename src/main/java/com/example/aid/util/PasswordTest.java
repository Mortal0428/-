package com.example.aid.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码测试工具
 */
public class PasswordTest {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 测试密码123456
        String password = "123456";
        String hashedPassword = encoder.encode(password);
        
        System.out.println("原始密码: " + password);
        System.out.println("生成的BCrypt哈希: " + hashedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(password, hashedPassword);
        System.out.println("密码验证结果: " + (matches ? "正确" : "错误"));
        
        // 测试一个已知的哈希值
        String knownHash = "$2a$10$7JB720yubVSOfvVWqVqj5eJ4Vj8vJqJqJqJqJqJqJqJqJqJqJqJqJq";
        boolean knownMatches = encoder.matches(password, knownHash);
        System.out.println("已知哈希验证结果: " + (knownMatches ? "正确" : "错误"));
    }
}
