package com.example.aid.service;

import com.example.aid.model.Application;
import com.example.aid.model.User;
import com.example.aid.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> findByStatus(String status) {
        return applicationRepository.findByStatus(status);
    }

    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }
    
    public List<Application> findByStudentAndType(User student, String type) {
        return applicationRepository.findByStudentAndType(student, type);
    }
    
    public List<Application> findByStudent(User student) {
        return applicationRepository.findByStudent(student);
    }
}


