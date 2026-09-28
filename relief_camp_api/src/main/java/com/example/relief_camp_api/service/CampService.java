package com.example.relief_camp_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.relief_camp_api.entity.Camp;
import com.example.relief_camp_api.repository.CampRepository;

@Service
public class CampService {
    private final CampRepository campRepository;

    public CampService(CampRepository campRepository) {
        this.campRepository = campRepository;
    }

    public Camp register(Camp camp) {
        camp.setId(null);
        return campRepository.save(camp);
    }

    public List<Camp> findAll() {
        return campRepository.findAll();
    }

    public Camp findById(Long id) {
        return campRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camp not found"));
    }
}