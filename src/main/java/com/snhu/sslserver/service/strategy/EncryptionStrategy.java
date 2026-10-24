package com.snhu.sslserver.service.strategy;

import java.io.InputStream;

/**
 * STRATEGY INTERFACE: Enforces a strict contract for data confidentiality.
 * Mandates two-way reversible cryptographic byte operations.
 */
public interface EncryptionStrategy {
    byte[] encrypt(InputStream dataStream, byte[] secretKey) throws Exception;
    byte[] decrypt(byte[] encryptedData, byte[] secretKey) throws Exception;
    String getAlgorithmName();
}
