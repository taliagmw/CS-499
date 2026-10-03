package com.snhu.sslserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ENHANCEMENT: Centralized Security Exception Interceptor.
 * Intercepts unhandled errors across the entire execution context and formats them securely.
 * Fulfills Program Outcome 5 (Security Mindset) by preventing internal stack trace leaks.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cryptographic environment validation failures gracefully.
     */
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<Map<String, Object>> handleCryptoException(NoSuchAlgorithmException ex) {
        Map<String, Object> errorPayload = new LinkedHashMap<>();
        
        // Sanitized, safe message that contains no system folder configurations or memory dumps
        errorPayload.put("timestamp", LocalDateTime.now());
        errorPayload.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorPayload.put("error", "Cryptographic Configuration Error");
        errorPayload.put("message", "The requested hashing or cipher algorithm is unsupported in this environment.");

        // Returns HTTP Status 500 with a clean machine-readable payload
        return new ResponseEntity<>(errorPayload, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Catch-all fallback guard to intercept any generic unexpected runtime failures safely.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> errorPayload = new LinkedHashMap<>();
        
        errorPayload.put("timestamp", LocalDateTime.now());
        errorPayload.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorPayload.put("error", "Internal Server Malfunction");
        errorPayload.put("message", "An unexpected processing error occurred. Please contact system administrators.");

        return new ResponseEntity<>(errorPayload, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
