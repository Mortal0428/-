package com.example.aid.service;

import com.example.aid.model.Publicity;
import com.example.aid.repository.PublicityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PublicityService {
    @Autowired
    private PublicityRepository publicityRepository;

    public List<Publicity> findAll() {
        return publicityRepository.findAll();
    }

    public Optional<Publicity> findById(Long id) {
        return publicityRepository.findById(id);
    }

    public Publicity save(Publicity publicity) {
        return publicityRepository.save(publicity);
    }
}



