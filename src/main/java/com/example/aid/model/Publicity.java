package com.example.aid.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "publicity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publicity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String objectionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objection_user_id")
    private User objectionUser;

    private LocalDateTime objectionTime;

    @Column(nullable = false, length = 20)
    private String status; // publicizing, no_objection, has_objection
}
