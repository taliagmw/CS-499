package com.snhu.sslserver.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * ENHANCEMENT: Relational Database Model Entity.
 * Maps transactional security metrics into the H2 database for persistent security auditing.
 * Meets CS 499 Category 3 (Databases) requirements.
 */
@Entity
@Table(name = "hash_audit_logs")
public class HashAuditLog {

    // Unique Primary Key utilizing an auto-incrementing identity strategy optimized for H2
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Captures the precise system timestamp when the data cryptographic integrity check was processed
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // Captures the dynamic client request identifier or user profile metadata initiating the signature
    @Column(nullable = false)
    private String clientMetadata;

    // Stores a safe, truncated summary of the incoming message payload to optimize table space allocation
    @Column(nullable = false, length = 1000)
    private String payloadSummary;

    // Records the selected encryption framework or hash routine choice (e.g., SHA-256, SHA-512, AES)
    @Column(nullable = false, length = 50)
    private String algorithmUsed;

    // Holds the final calculated cryptographic signature output string
    @Column(nullable = false, length = 128)
    private String checksumValue;

    // Standard default constructor mandatory for Hibernate JPA instantiation operations
    public HashAuditLog() {}

    // Overloaded tracking constructor to streamline log creation within the Service tier
    public HashAuditLog(String clientMetadata, String payloadSummary, String algorithmUsed, String checksumValue) {
        this.timestamp = LocalDateTime.now(); // Automatically stamp log generation time
        this.clientMetadata = clientMetadata;
        this.payloadSummary = payloadSummary;
        this.algorithmUsed = algorithmUsed;
        this.checksumValue = checksumValue;
    }

    // =========================================================================
    // GETTERS AND SETTERS: Standard clean access methods for Object-Relational mapping
    // =========================================================================
    public Long getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getClientMetadata() { return clientMetadata; }
    public void setClientMetadata(String clientMetadata) { this.clientMetadata = clientMetadata; }
    public String getPayloadSummary() { return payloadSummary; }
    public void setPayloadSummary(String payloadSummary) { this.payloadSummary = payloadSummary; }
    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }
    public String getChecksumValue() { return checksumValue; }
    public void setChecksumValue(String checksumValue) { this.checksumValue = checksumValue; }
}
