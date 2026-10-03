// File: src/main/java/com/snhu/sslserver/controller/ChecksumController.java
package com.snhu.sslserver.controller;

import com.snhu.sslserver.entity.AuditLog;
import com.snhu.sslserver.repository.AuditLogRepository;
import com.snhu.sslserver.service.CryptographyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ChecksumController {

    private final CryptographyService cryptoService;
    private final AuditLogRepository auditLogRepository;

    public ChecksumController(CryptographyService cryptoService, AuditLogRepository auditLogRepository) {
        this.cryptoService = cryptoService;
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/hash")
    public ResponseEntity<Map<String, Object>> getHashSignature() {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        
        try {
            String dataPayload = "Hello World Check Sum! Talia McCarthy-Wielenga";
            byte[] rawBytes = dataPayload.getBytes(StandardCharsets.UTF_8);
            byte[] symmetricTestingKey = "SNHUCapstone2026".getBytes(StandardCharsets.UTF_8);

            ByteArrayInputStream hashStream = new ByteArrayInputStream(rawBytes);
            String computedChecksum = cryptoService.computeIntegrityCheck(hashStream);

            ByteArrayInputStream encryptionStream = new ByteArrayInputStream(rawBytes);
            byte[] encryptedBytes = cryptoService.executeConfidentialEncryption(encryptionStream, symmetricTestingKey);
            String base64Ciphertext = Base64.getEncoder().encodeToString(encryptedBytes);

            // Commit the logging transaction straight to the persistent H2 storage disk
            AuditLog logEntry = new AuditLog(
                "Talia McCarthy-Wielenga", 
                "VALIDATE_CRYPTO_STREAM", 
                "SHA-256", 
                "AES-CBC", 
                "SUCCESS"
            );
            auditLogRepository.save(logEntry);

            responseBody.put("ownerMetadata", "Talia McCarthy-Wielenga");
            responseBody.put("originalData", dataPayload);
            responseBody.put("integrityAlgorithm", "SHA-256");
            responseBody.put("checksumValue", computedChecksum);
            responseBody.put("confidentialityAlgorithm", "AES-CBC");
            responseBody.put("encryptedPayloadBase64", base64Ciphertext);
            responseBody.put("databasePersistenceStatus", "COMMITTED_TO_FILE_DISK_SUCCESS");

            return ResponseEntity.ok(responseBody);

        } catch (Exception ex) {
            throw new RuntimeException("Cryptographic database transaction lifecycle failure.", ex);
        }
    }
}

