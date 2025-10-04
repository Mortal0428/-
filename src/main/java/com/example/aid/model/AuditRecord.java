package com.example.aid.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(nullable = false, length = 20)
    private String role; // counselor, group, dean

    @Column(nullable = false, length = 20)
    private String result; // approved, rejected

    @Column(columnDefinition = "TEXT")
    private String reason;

    private LocalDateTime reviewTime;

    @PrePersist
    public void prePersist() {
        this.reviewTime = LocalDateTime.now();
    }
}
