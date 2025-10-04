package com.example.aid.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bind_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BindRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    @Column(nullable = false, length = 20)
    private String status; // pending, approved, rejected

    private LocalDateTime requestTime;

    @PrePersist
    public void prePersist() {
        this.requestTime = LocalDateTime.now();
    }
}
