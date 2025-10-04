package com.example.aid.repository;

import com.example.aid.model.Application;
import com.example.aid.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStatus(String status);
    List<Application> findByStudentAndType(User student, String type);
    List<Application> findByStudent(User student);
}


