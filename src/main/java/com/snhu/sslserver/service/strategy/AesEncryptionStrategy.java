package com.snhu.sslserver.service.strategy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.nio.ByteBuffer;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * FIXED CONCRETE STRATEGY: Implements Authenticated Randomized Encryption.
 * Employs AES-GCM-NoPadding with cryptographically secure, randomized 96-bit IV generation.
 * Eradicates information disclosure leaks and pattern duplication inherent to default modes.
 */
public class AesEncryptionStrategy implements EncryptionStrategy {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ALGORITHM = "AES";
    
    private static final int GCM_IV_LENGTH = 12;     // Secure standard 96-bit GCM IV
    private static final int GCM_TAG_LENGTH = 128;   // 128-bit authentication tag

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String getAlgorithmName() {
        return "AES-GCM";
    }

    @Override
    public byte[] encrypt(InputStream dataStream, byte[] secretKey) throws Exception {
        if (secretKey == null || secretKey.length != 16) {
            throw new IllegalArgumentException("AES-GCM requires a valid 128-bit (16 byte) secret key.");
        }

        // FIXED: Synthesize a completely randomized IV for every unique execution block
        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);

        SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
        
        // Sequentially stream bytes from memory block-by-block
        ByteArrayOutputStream streamBuffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataStream.read(chunk)) != -1) {
            streamBuffer.write(chunk, 0, bytesRead);
        }
        
        byte[] ciphertext = cipher.doFinal(streamBuffer.toByteArray());

        // Prefix the randomized IV straight onto the ciphertext block for decryption portability
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertext.length);
        byteBuffer.put(iv);
        byteBuffer.put(ciphertext);
        
        return byteBuffer.array();
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, byte[] secretKey) throws Exception {
        if (secretKey == null || secretKey.length != 16) {
            throw new IllegalArgumentException("AES-GCM requires a valid 128-bit secret key.");
        }

        // Extract the unique IV from the payload array prefix boundary
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);
        
        byte[] ciphertext = new byte[byteBuffer.remaining()];
        byteBuffer.get(ciphertext);

        SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
        
        return cipher.doFinal(ciphertext);
    }
}

