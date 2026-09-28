package com.example.forest_fire_management.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fire_incidents")
public class FireIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String status;

    private String assignedRanger;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reportedAt;

    protected FireIncident() {
    }

    public FireIncident(String location, String severity) {
        this.location = location;
        this.severity = severity;
        this.status = "REPORTED";
        this.reportedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getSeverity() {
        return severity;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedRanger() {
        return assignedRanger;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void dispatchTo(String rangerName) {
        this.assignedRanger = rangerName;
        this.status = "DISPATCHED";
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}