package com.snhu.sslserver.service;

import com.snhu.sslserver.service.strategy.HashStrategy;
import com.snhu.sslserver.service.strategy.EncryptionStrategy;
import com.snhu.sslserver.service.strategy.Sha256HashStrategy;
import com.snhu.sslserver.service.strategy.AesEncryptionStrategy;
import org.springframework.stereotype.Service;
import java.io.InputStream;

/**
 * ENHANCEMENT TIER: Centralized Cryptographic Coordinator Service.
 * Orchestrates multi-tiered checking pipelines using abstract interfaces.
 */
@Service
public class CryptographyService {

    private HashStrategy hashStrategy;
    private EncryptionStrategy encryptionStrategy;

    public CryptographyService() {
        this.hashStrategy = new Sha256HashStrategy();
        this.encryptionStrategy = new AesEncryptionStrategy();
    }

    public void setHashStrategy(HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
    }

    public void setEncryptionStrategy(EncryptionStrategy encryptionStrategy) {
        this.encryptionStrategy = encryptionStrategy;
    }

    public String computeIntegrityCheck(InputStream payloadStream) throws Exception {
        if (payloadStream == null) {
            throw new IllegalArgumentException("Inbound streaming asset cannot be null.");
        }
        try (payloadStream) { // Explicitly guarantees socket stream boundary closure
            return hashStrategy.generateChecksum(payloadStream);
        }
    }

    public byte[] executeConfidentialEncryption(InputStream payloadStream, byte[] secretKey) throws Exception {
        if (payloadStream == null || secretKey == null) {
            throw new IllegalArgumentException("Cipher streaming metrics arguments cannot be null.");
        }
        try (payloadStream) {
            return encryptionStrategy.encrypt(payloadStream, secretKey);
        }
    }
}
