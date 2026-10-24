package com.snhu.sslserver.service.strategy;

import java.io.InputStream;
import java.security.MessageDigest;

/**
 * CONCRETE STRATEGY: Implements memory-optimized SHA-256 message digests.
 * Streams data sequentially via a 1KB block buffer to maintain a near-zero memory footprint.
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
            throw new IllegalArgumentException("Inbound data stream cannot be null.");
        }

        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] buffer = new byte[1024]; // 1KB optimized chunk size
        int bytesRead;

        // Process data chunk-by-chunk to maximize resource memory efficiency
        while ((bytesRead = dataStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        // Convert the raw bytes into a secure hex string via bitwise lookups
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
