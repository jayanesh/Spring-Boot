package com.example.forest_fire_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forest_fire_management.model.FireIncident;

public interface FireIncidentRepository extends JpaRepository<FireIncident, Long> {
    List<FireIncident> findAllByOrderByReportedAtDesc();
}