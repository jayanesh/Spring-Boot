package com.example.relief_camp_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.relief_camp_api.entity.Distribution;
import com.example.relief_camp_api.entity.Family;
import com.example.relief_camp_api.entity.Supply;
import com.example.relief_camp_api.repository.DistributionRepository;
import com.example.relief_camp_api.repository.FamilyRepository;

@Service
public class DistributionService {
    private final CampService campService;
    private final FamilyRepository familyRepository;
    private final SupplyService supplyService;
    private final DistributionRepository distributionRepository;

    public DistributionService(CampService campService, FamilyRepository familyRepository, SupplyService supplyService,
                               DistributionRepository distributionRepository) {
        this.campService = campService;
        this.familyRepository = familyRepository;
        this.supplyService = supplyService;
        this.distributionRepository = distributionRepository;
    }

    @Transactional
    public Distribution distribute(Long familyId, Long supplyId, int quantity) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found"));
        Supply supply = supplyService.findById(supplyId);

        if (!family.isHoused()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Family is not currently housed");
        }
        if (!family.getCamp().getId().equals(supply.getCamp().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Family and supply must belong to the same camp");
        }
        if (quantity < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be at least 1");
        }
        if (supply.getQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough supply in camp inventory");
        }

        supply.setQuantity(supply.getQuantity() - quantity);
        supplyService.save(supply);

        Distribution distribution = new Distribution();
        distribution.setFamily(family);
        distribution.setSupply(supply);
        distribution.setQuantity(quantity);
        return distributionRepository.save(distribution);
    }

    public List<Distribution> findForCamp(Long campId) {
        campService.findById(campId);
        return distributionRepository.findBySupplyCampId(campId);
    }
}