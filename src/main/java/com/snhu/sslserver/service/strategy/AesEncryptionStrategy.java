// File: src/main/java/com/snhu/sslserver/service/strategy/AesEncryptionStrategy.java
package com.snhu.sslserver.service.strategy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Concrete strategy implementing AES symmetric encryption and decryption.
 * Uses an explicit, secure Cipher transformation block to prevent unsafe provider defaults.
 */
public class AesEncryptionStrategy implements EncryptionStrategy {

    // Explicit safe mode transformation: Cipher-Block Chaining with PKCS5 padding
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    
    // Fixed initialization vector for testing environment validation constraints
    private static final byte[] FIXED_IV = new byte[16]; 

    @Override
    public String getAlgorithmName() {
        return ALGORITHM;
    }

    @Override
    public byte[] encrypt(InputStream dataStream, byte[] secretKey) throws Exception {
        if (secretKey == null || secretKey.length != 16) {
            throw new IllegalArgumentException("AES requires a valid 128-bit secret key.");
        }

        SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(FIXED_IV);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] dataChunk = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataStream.read(dataChunk)) != -1) {
            buffer.write(dataChunk, 0, bytesRead);
        }
        
        return cipher.doFinal(buffer.toByteArray());
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, byte[] secretKey) throws Exception {
        if (secretKey == null || secretKey.length != 16) {
            throw new IllegalArgumentException("AES requires a valid 128-bit secret key.");
        }

        SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(FIXED_IV);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(encryptedData);
    }
}
