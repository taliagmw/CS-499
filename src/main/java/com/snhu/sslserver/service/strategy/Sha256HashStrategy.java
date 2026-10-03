package com.snhu.sslserver.service.strategy;

import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Concrete strategy implementing SHA-256 cryptographic message digests.
 * Streams data sequentially to maintain a near-zero memory footprint during execution.
 */
public class Sha256HashStrategy implements HashStrategy {

    private static final String ALGORITHM = "SHA-256";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM;
    }

    @Override
    public String generateChecksum(InputStream dataStream) throws Exception {
        if (dataStream == null) {
            throw new IllegalArgumentException("Input data stream cannot be null.");
        }

        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] buffer = new byte[1024]; // 1KB sequential streaming buffer block
        int bytesRead;

        // Process data streams chunk-by-chunk to maximize memory overhead efficiency
        while ((bytesRead = dataStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        // Convert the raw binary digest bytes into a readable Hexadecimal format
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
