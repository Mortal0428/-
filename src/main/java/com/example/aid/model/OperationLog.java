package com.example.aid.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operation_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 20)
    private String role;

    @Column(length = 50)
    private String operationType;

    @Column(columnDefinition = "TEXT")
    private String operationContent;

    private LocalDateTime operationTime;

    @PrePersist
    public void prePersist() {
        this.operationTime = LocalDateTime.now();
    }
}
