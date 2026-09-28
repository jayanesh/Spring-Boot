package com.example.forest_fire_management.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.forest_fire_management.model.FireIncident;
import com.example.forest_fire_management.repository.FireIncidentRepository;

@RestController
@RequestMapping("/api/incidents")
public class FireIncidentController {

    private static final Set<String> SEVERITIES = Set.of("LOW", "MEDIUM", "HIGH", "CRITICAL");
    private static final Set<String> STATUSES = Set.of("REPORTED", "DISPATCHED", "CONTAINED", "RESOLVED");

    private final FireIncidentRepository incidentRepository;

    public FireIncidentController(FireIncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @GetMapping
    public List<FireIncident> getIncidents() {
        return incidentRepository.findAllByOrderByReportedAtDesc();
    }

    @PostMapping
    public ResponseEntity<FireIncident> createIncident(@RequestBody CreateIncidentRequest request) {
        if (request.location() == null || request.location().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is required");
        }

        String severity = request.severity() == null ? "" : request.severity().toUpperCase();
        if (!SEVERITIES.contains(severity)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a valid severity");
        }

        FireIncident incident = new FireIncident(request.location().trim(), severity);
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentRepository.save(incident));
    }

    @PatchMapping("/{id}/dispatch")
    public FireIncident dispatchIncident(@PathVariable Long id, @RequestBody DispatchRequest request) {
        if (request.rangerName() == null || request.rangerName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ranger name is required");
        }

        FireIncident incident = findIncident(id);
        incident.dispatchTo(request.rangerName().trim());
        return incidentRepository.save(incident);
    }

    @PatchMapping("/{id}/status")
    public FireIncident updateIncidentStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        String status = request.status() == null ? "" : request.status().toUpperCase();
        if (!STATUSES.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a valid incident status");
        }

        FireIncident incident = findIncident(id);
        incident.updateStatus(status);
        return incidentRepository.save(incident);
    }

    private FireIncident findIncident(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident not found"));
    }

    public record CreateIncidentRequest(String location, String severity) {
    }

    public record DispatchRequest(String rangerName) {
    }

    public record StatusRequest(String status) {
    }
}