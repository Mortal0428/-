package com.example.aid.repository;

import com.example.aid.model.AuditRecord;
import com.example.aid.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditRecordRepository extends JpaRepository<AuditRecord, Long> {
    List<AuditRecord> findByApplication(Application application);
    List<AuditRecord> findByApplicationOrderByReviewTimeDesc(Application application);
    List<AuditRecord> findByApplicationAndRoleOrderByReviewTimeDesc(Application application, String role);
}
