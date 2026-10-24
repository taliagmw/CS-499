package com.snhu.sslserver.controller;

import com.snhu.sslserver.entity.AuditLog;
import com.snhu.sslserver.repository.AuditLogRepository;
import com.snhu.sslserver.service.CryptographyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DECOUPLED WEB TIER: Web Request Routing Controller.
 * Ingests incoming parameters, wraps strings as InputStreams, and saves logs to disk.
 */
@RestController
public class ChecksumController {

    private final CryptographyService cryptoService;
    private final AuditLogRepository auditLogRepository;

    // FIXED: Inject security key properties dynamically from external configuration files
    @Value("${app.security.secret-key}")
    private String configuredSecretKey;

    public ChecksumController(CryptographyService cryptoService, AuditLogRepository auditLogRepository) {
        this.cryptoService = cryptoService;
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/hash")
    public ResponseEntity<Map<String, Object>> getHashSignature() {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        
        try {
            // Core milestone validation diagnostic metadata text payload
            String dataPayload = "Hello World Check Sum! Talia McCarthy-Wielenga";
            byte[] rawBytes = dataPayload.getBytes(StandardCharsets.UTF_8);
            
            // Extract fixed-size byte slices safely from your injected properties text configuration
            byte[] symmetricTestingKey = configuredSecretKey.substring(0, 16).getBytes(StandardCharsets.UTF_8);

            // Pathway A: Stream verification processing for SHA-256 Data Integrity
            ByteArrayInputStream hashStream = new ByteArrayInputStream(rawBytes);
            String computedChecksum = cryptoService.computeIntegrityCheck(hashStream);

            // Pathway B: Stream verification processing for AES-GCM Confidentiality
            ByteArrayInputStream encryptionStream = new ByteArrayInputStream(rawBytes);
            byte[] encryptedBytes = cryptoService.executeConfidentialEncryption(encryptionStream, symmetricTestingKey);
            String base64Ciphertext = Base64.getEncoder().encodeToString(encryptedBytes);

            // PERSISTENCE TRANSACTION: Safely serialize log entries straight onto physical storage
            AuditLog logEntry = new AuditLog(
                "Talia McCarthy-Wielenga", 
                "VALIDATE_CRYPTO_STREAM", 
                "SHA-256", 
                "AES-GCM", 
                "SUCCESS"
            );
            auditLogRepository.save(logEntry);

            // Build structural, cross-platform machine-readable JSON fields
            responseBody.put("ownerMetadata", "Talia McCarthy-Wielenga");
            responseBody.put("originalData", dataPayload);
            responseBody.put("integrityAlgorithm", "SHA-256");
            responseBody.put("checksumValue", computedChecksum);
            responseBody.put("confidentialityAlgorithm", "AES-GCM (Authenticated)");
            responseBody.put("encryptedPayloadBase64", base64Ciphertext);
            responseBody.put("databasePersistenceStatus", "COMMITTED_TO_FILE_DISK_SUCCESS");

            return ResponseEntity.ok(responseBody);

        } catch (Exception ex) {
            throw new RuntimeException("Cryptographic microservice endpoint lifecycle processing failure.", ex);
        }
    }
}
