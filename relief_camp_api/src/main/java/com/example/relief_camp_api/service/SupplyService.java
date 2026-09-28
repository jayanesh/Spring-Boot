package com.example.relief_camp_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.relief_camp_api.entity.Camp;
import com.example.relief_camp_api.entity.Supply;
import com.example.relief_camp_api.repository.SupplyRepository;

@Service
public class SupplyService {
    private final CampService campService;
    private final SupplyRepository supplyRepository;

    public SupplyService(CampService campService, SupplyRepository supplyRepository) {
        this.campService = campService;
        this.supplyRepository = supplyRepository;
    }

    @Transactional
    public Supply receive(Long campId, Supply incomingSupply) {
        Camp camp = campService.findById(campId);
        incomingSupply.setId(null);

        Supply supply = supplyRepository.findByCampIdAndType(campId, incomingSupply.getType())
                .orElseGet(Supply::new);
        supply.setCamp(camp);
        supply.setType(incomingSupply.getType());
        supply.setQuantity(supply.getQuantity() + incomingSupply.getQuantity());
        return supplyRepository.save(supply);
    }

    public List<Supply> findForCamp(Long campId) {
        campService.findById(campId);
        return supplyRepository.findByCampId(campId);
    }

    public Supply findById(Long id) {
        return supplyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supply not found"));
    }

    public Supply save(Supply supply) {
        return supplyRepository.save(supply);
    }
}