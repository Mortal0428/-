package com.example.aid.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "material")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = false, length = 255)
    private String filePath;

    @Column(length = 100)
    private String description;

    private LocalDateTime uploadTime;

    @PrePersist
    public void prePersist() {
        this.uploadTime = LocalDateTime.now();
    }
}
