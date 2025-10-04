package com.example.aid.service;

import com.example.aid.model.Application;
import com.example.aid.model.AuditRecord;
import com.example.aid.model.User;
import com.example.aid.repository.ApplicationRepository;
import com.example.aid.repository.AuditRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ReviewService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private AuditRecordRepository auditRecordRepository;

    public void review(Application app, User reviewer, String role, String result, String reason, String nextStatusIfApproved) {
        AuditRecord ar = new AuditRecord();
        ar.setApplication(app);
        ar.setReviewer(reviewer);
        ar.setRole(role);
        ar.setResult(result);
        ar.setReason(reason);
        ar.setReviewTime(LocalDateTime.now());
        auditRecordRepository.save(ar);

        if ("approved".equals(result)) {
            app.setStatus(nextStatusIfApproved);
        } else {
            app.setStatus("rejected");
        }
        app.setUpdateTime(LocalDateTime.now());
        applicationRepository.save(app);
    }
}


