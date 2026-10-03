package com.snhu.sslserver.service.strategy;

import java.io.InputStream;

/**
 * Strategy contract for computing reversible symmetric encryption operations.
 * This explicitly separates data confidentiality mechanisms from integrity checking (hashing).
 */
public interface EncryptionStrategy {

    /**
     * Processes an incoming stream of cleartext data and encrypts it using a symmetric key.
     *
     * @param dataStream The live incoming stream of data to be encrypted
     * @param secretKey  The symmetric key material used for the cipher operation
     * @return A byte array containing the encrypted ciphertext payload
     * @throws Exception If a cryptographic or cipher initialization error occurs
     */
    byte[] encrypt(InputStream dataStream, byte[] secretKey) throws Exception;

    /**
     * Decrypts an encrypted ciphertext payload back into cleartext bytes.
     *
     * @param encryptedData The raw encrypted ciphertext bytes
     * @param secretKey     The symmetric key material used for decryption
     * @return A byte array containing the restored original cleartext data
     * @throws Exception If decryption or padding verification fails
     */
    byte[] decrypt(byte[] encryptedData, byte[] secretKey) throws Exception;

    /**
     * Retrieves the official standard name of the cryptographic encryption algorithm.
     * 
     * @return The cipher identifier string (e.g., "AES")
     */
    String getAlgorithmName();
}
