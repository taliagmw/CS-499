// File: src/main/java/com/snhu/sslserver/entity/AuditLog.java
package com.snhu.sslserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * DATABASE LAYER: Object-Relational Mapping Entity.
 * Maps cryptographic telemetry metrics directly onto physical file-backed disk tables.
 */
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;
    private String action;
    private String hashAlgorithm;
    private String confidentialityAlgorithm;
    private String status;
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(String owner, String action, String hashAlgorithm, String confidentialityAlgorithm, String status) {
        this.owner = owner;
        this.action = action;
        this.hashAlgorithm = hashAlgorithm;
        this.confidentialityAlgorithm = confidentialityAlgorithm;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getHashAlgorithm() { return hashAlgorithm; }
    public void setHashAlgorithm(String hashAlgorithm) { this.hashAlgorithm = hashAlgorithm; }
    public String getConfidentialityAlgorithm() { return confidentialityAlgorithm; }
    public void setConfidentialityAlgorithm(String confidentialityAlgorithm) { this.confidentialityAlgorithm = confidentialityAlgorithm; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
