package com.example.relief_camp_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.relief_camp_api.entity.Camp;
import com.example.relief_camp_api.entity.Family;
import com.example.relief_camp_api.repository.FamilyRepository;

@Service
public class FamilyService {
    private final CampService campService;
    private final FamilyRepository familyRepository;

    public FamilyService(CampService campService, FamilyRepository familyRepository) {
        this.campService = campService;
        this.familyRepository = familyRepository;
    }

    @Transactional
    public Family checkIn(Long campId, Family family) {
        Camp camp = campService.findById(campId);
        int occupiedPlaces = familyRepository.findByCampIdAndHousedTrue(campId).stream()
                .mapToInt(Family::getHeadcount)
                .sum();
        if (occupiedPlaces + family.getHeadcount() > camp.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Camp does not have enough capacity");
        }

        family.setId(null);
        family.setCamp(camp);
        family.setHoused(true);
        return familyRepository.save(family);
    }

    public List<Family> findHousedAtCamp(Long campId) {
        campService.findById(campId);
        return familyRepository.findByCampIdAndHousedTrue(campId);
    }

    @Transactional
    public Family checkOut(Long campId, Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found"));
        if (!family.getCamp().getId().equals(campId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found in this camp");
        }
        family.setHoused(false);
        return familyRepository.save(family);
    }
}