package com.snhu.sslserver.service.strategy;

import java.io.InputStream;

/**
 * STRATEGY INTERFACE: Enforces a strict contract for data integrity checksums.
 * Separates non-reversible message digests from reversible encryption blocks.
 */
public interface HashStrategy {
    String generateChecksum(InputStream dataStream) throws Exception;
    String getAlgorithmName();
}
