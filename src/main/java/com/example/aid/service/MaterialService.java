package com.example.aid.service;

import com.example.aid.model.Application;
import com.example.aid.model.Material;
import com.example.aid.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> findByApplication(Application application) {
        return materialRepository.findByApplication(application);
    }

    public Material save(Material material) {
        return materialRepository.save(material);
    }
}


