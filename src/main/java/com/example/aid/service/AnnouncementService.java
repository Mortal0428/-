package com.example.aid.service;

import com.example.aid.model.Announcement;
import com.example.aid.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<Announcement> findAll() {
        return announcementRepository.findAll();
    }

    public Optional<Announcement> findById(Long id) {
        return announcementRepository.findById(id);
    }

    public Announcement save(Announcement announcement) {
        return announcementRepository.save(announcement);
    }
}



