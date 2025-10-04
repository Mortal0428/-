package com.example.aid.repository;

import com.example.aid.model.Material;
import com.example.aid.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByApplication(Application application);
}


