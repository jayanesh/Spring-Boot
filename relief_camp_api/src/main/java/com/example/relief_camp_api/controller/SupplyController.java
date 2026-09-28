package com.example.relief_camp_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.relief_camp_api.entity.Supply;
import com.example.relief_camp_api.service.SupplyService;

@RestController
@RequestMapping("/api/camps/{campId}")
public class SupplyController {
    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @PostMapping("/supplies")
    public ResponseEntity<Supply> receive(@PathVariable Long campId, @RequestBody Supply supply) {
        Supply savedSupply = supplyService.receive(campId, supply);
        return ResponseEntity.created(URI.create("/api/camps/" + campId + "/supplies/" + savedSupply.getId()))
                .body(savedSupply);
    }

    @GetMapping("/inventory")
    public List<Supply> findInventory(@PathVariable Long campId) {
        return supplyService.findForCamp(campId);
    }
}