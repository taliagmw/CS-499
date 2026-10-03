package com.snhu.sslserver.service.strategy;

import java.io.InputStream;

/**
 * Strategy contract for computing non-reversible cryptographic message digests.
 * This separates data integrity operations from confidentiality encryption mechanisms.
 */
public interface HashStrategy {

    /**
     * Dynamically processes an incoming data stream to generate an integrity checksum.
     * By using an InputStream, large payloads can be handled sequentially without
     * exhausting server memory buffers.
     *
     * @param dataStream The live incoming stream of data to be verified
     * @return A hexadecimal string representation of the computed hash digest
     * @throws Exception If a cryptographic or stream reading error occurs
     */
    String generateChecksum(InputStream dataStream) throws Exception;

    /**
     * Retrieves the official standard name of the cryptographic algorithm.
     * 
     * @return The algorithm identifier string (e.g., "SHA-256")
     */
    String getAlgorithmName();
}
