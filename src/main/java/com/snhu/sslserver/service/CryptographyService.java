package com.snhu.sslserver.service;

import com.snhu.sslserver.service.strategy.HashStrategy;
import com.snhu.sslserver.service.strategy.EncryptionStrategy;
import com.snhu.sslserver.service.strategy.Sha256HashStrategy;
import com.snhu.sslserver.service.strategy.AesEncryptionStrategy;
import org.springframework.stereotype.Service;
import java.io.InputStream;

/**
 * ENHANCEMENT: Architectural Tier for Algorithmic Strategy Orchestration.
 * Manages low-level cryptographic data calculations through decoupled strategy interfaces.
 * Fulfills CS 499 Category 2 (Algorithms and Data Structures) metrics by separating 
 * integrity checksum paths from data confidentiality operations and streaming data via InputStreams.
 */
@Service
public class CryptographyService {

    private HashStrategy hashStrategy;
    private EncryptionStrategy encryptionStrategy;

    /**
     * Default constructor initializing safe, industry-standard cryptographic defaults.
     */
    public CryptographyService() {
        this.hashStrategy = new Sha256HashStrategy();
        this.encryptionStrategy = new AesEncryptionStrategy();
    }

    /**
     * Dynamic runtime mutator to swap out hash mechanisms (Algorithmic Adaptability).
     * @param hashStrategy Concrete data integrity implementation
     */
    public void setHashStrategy(HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
    }

    /**
     * Dynamic runtime mutator to swap out symmetric ciphers (Algorithmic Adaptability).
     * @param encryptionStrategy Concrete data confidentiality implementation
     */
    public void setEncryptionStrategy(EncryptionStrategy encryptionStrategy) {
        this.encryptionStrategy = encryptionStrategy;
    }

    /**
     * Computes a cryptographic checksum for a stream of data using the assigned strategy.
     * Enforces explicit boundary management via try-with-resources.
     * 
     * @param payloadStream Live incoming sequential data stream
     * @return Hexadecimal representation signature string
     * @throws Exception If stream resolution or cryptographic verification fails
     */
    public String computeIntegrityCheck(InputStream payloadStream) throws Exception {
        if (payloadStream == null) {
            throw new IllegalArgumentException("Inbound payload stream asset cannot be null.");
        }
        try (payloadStream) { // Guarantees stream socket resource disposal post-execution
            return hashStrategy.generateChecksum(payloadStream);
        }
    }

    /**
     * Encrypts an incoming data stream using the assigned symmetric strategy.
     * 
     * @param payloadStream Live incoming data stream to protect
     * @param secretKey 128-bit symmetric key material
     * @return Encrypted ciphertext byte array
     * @throws Exception If cipher configuration or streaming errors occur
     */
    public byte[] executeConfidentialEncryption(InputStream payloadStream, byte[] secretKey) throws Exception {
        if (payloadStream == null || secretKey == null) {
            throw new IllegalArgumentException("Symmetric cipher streaming arguments cannot be null.");
        }
        try (payloadStream) {
            return encryptionStrategy.encrypt(payloadStream, secretKey);
        }
    }

    /**
     * Decrypts a ciphertext byte array back into its raw cleartext state.
     * 
     * @param encryptedData Ciphertext payload
     * @param secretKey 128-bit symmetric key material
     * @return Restored original cleartext bytes
     * @throws Exception If padding or cipher keys are invalid
     */
    public byte[] executeConfidentialDecryption(byte[] encryptedData, byte[] secretKey) throws Exception {
        if (encryptedData == null || secretKey == null) {
            throw new IllegalArgumentException("Symmetric decipher block arguments cannot be null.");
        }
        return encryptionStrategy.decrypt(encryptedData, secretKey);
    }
}
