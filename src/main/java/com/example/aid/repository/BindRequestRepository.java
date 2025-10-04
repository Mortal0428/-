package com.example.aid.repository;

import com.example.aid.model.BindRequest;
import com.example.aid.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BindRequestRepository extends JpaRepository<BindRequest, Long> {
    List<BindRequest> findByCounselorAndStatus(User counselor, String status);
    List<BindRequest> findByCounselor(User counselor);
    List<BindRequest> findByStudent(User student);
    Optional<BindRequest> findTopByStudentOrderByRequestTimeDesc(User student);
}
