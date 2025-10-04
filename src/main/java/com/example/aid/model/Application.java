package com.example.aid.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false, length = 20)
    private String type; // grant, scholarship

    @Column(nullable = false, length = 30)
    private String status; // draft, submitted, counselor_review, group_review, dean_review, publicity, approved, rejected, appeal

    private LocalDateTime submitTime;
    private LocalDateTime updateTime;

    @Column(columnDefinition = "TEXT")
    private String reason;
}
