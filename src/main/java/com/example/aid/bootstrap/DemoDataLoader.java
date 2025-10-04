package com.example.aid.bootstrap;

import com.example.aid.model.*;
import com.example.aid.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DemoDataLoader {
    @Bean
    CommandLineRunner loadDemoData(UserRepository userRepository,
                                   AnnouncementRepository announcementRepository,
                                   ApplicationRepository applicationRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Users
            User admin = userRepository.findByUsername("admin").orElseGet(() -> {
                User u = new User();
                u.setUsername("admin");
                u.setPassword(passwordEncoder.encode("admin123"));
                u.setName("系统管理员");
                u.setRole("admin");
                u.setCollege("资助中心");
                return userRepository.save(u);
            });
            User counselor = userRepository.findByUsername("counselor1").orElseGet(() -> {
                User u = new User();
                u.setUsername("counselor1");
                u.setPassword(passwordEncoder.encode("counselor123"));
                u.setName("辅导员A");
                u.setRole("counselor");
                u.setStaffNo("T1001");
                u.setCollege("计算机学院");
                return userRepository.save(u);
            });
            User group = userRepository.findByUsername("group1").orElseGet(() -> {
                User u = new User();
                u.setUsername("group1");
                u.setPassword(passwordEncoder.encode("group123"));
                u.setName("评议组员");
                u.setRole("group");
                u.setStaffNo("G2001");
                u.setCollege("计算机学院");
                return userRepository.save(u);
            });
            User dean = userRepository.findByUsername("dean1").orElseGet(() -> {
                User u = new User();
                u.setUsername("dean1");
                u.setPassword(passwordEncoder.encode("dean123"));
                u.setName("院长");
                u.setRole("dean");
                u.setStaffNo("D3001");
                u.setCollege("计算机学院");
                return userRepository.save(u);
            });
            userRepository.findByUsername("student1").orElseGet(() -> {
                User u = new User();
                u.setUsername("student1");
                u.setPassword(passwordEncoder.encode("student123"));
                u.setName("学生甲");
                u.setRole("student");
                u.setStudentNo("S5001");
                u.setClassName("计科22-1");
                u.setCollege("计算机学院");
                return userRepository.save(u);
            });

            // Announcement
            if (announcementRepository.count() == 0) {
                Announcement a = new Announcement();
                a.setTitle("国家励志奖学金申请公告");
                a.setContent("请按要求提交申请与材料。");
                a.setCreator(admin);
                announcementRepository.save(a);
            }

            // Applications for each stage to populate lists
            if (applicationRepository.count() == 0) {
                User student = userRepository.findByUsername("student1").orElseThrow();
                Application submitted = new Application();
                submitted.setStudent(student);
                submitted.setType("grant");
                submitted.setReason("家庭困难，申请助学金。");
                submitted.setStatus("submitted");
                submitted.setSubmitTime(LocalDateTime.now());
                submitted.setUpdateTime(LocalDateTime.now());
                applicationRepository.save(submitted);

                Application groupStage = new Application();
                groupStage.setStudent(student);
                groupStage.setType("scholarship");
                groupStage.setReason("综合成绩优秀，申请国家励志奖学金。");
                groupStage.setStatus("group_review");
                groupStage.setSubmitTime(LocalDateTime.now());
                groupStage.setUpdateTime(LocalDateTime.now());
                applicationRepository.save(groupStage);

                Application deanStage = new Application();
                deanStage.setStudent(student);
                deanStage.setType("grant");
                deanStage.setReason("材料齐全，等待院长终审。");
                deanStage.setStatus("dean_review");
                deanStage.setSubmitTime(LocalDateTime.now());
                deanStage.setUpdateTime(LocalDateTime.now());
                applicationRepository.save(deanStage);
            }
        };
    }
}
