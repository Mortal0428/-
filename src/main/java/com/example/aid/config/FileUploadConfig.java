package com.example.aid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 使用绝对路径确保目录存在
        File uploadDirectory = new File(System.getProperty("user.dir") + File.separator + "uploads");
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        
        // 配置静态资源映射
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDirectory.getAbsolutePath() + "/");
    }
}