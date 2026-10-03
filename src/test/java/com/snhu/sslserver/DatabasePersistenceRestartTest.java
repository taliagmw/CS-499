package com.snhu.sslserver;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * INTEGRATION TEST: Database Persistence Restart Verification.
 * Proves that audit records survive a full application context teardown and restart.
 * Satisfies the Milestone Three feedback requirements for structural persistence evidence.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabasePersistenceRestartTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * STEP 1: Initialize the schema table structure and insert an audit record.
     * DirtiesContext forces Spring to completely kill and rebuild the application context
     * after this method completes, simulating a severe system crash or server reboot.
     */
    @Test
    @Order(1)
    @DirtiesContext
    public void step1_writeAuditRecordAndRebootServer() {
        // Formulate a robust audit schema layout table structure
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS audit_log (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "owner VARCHAR(255), " +
                "action VARCHAR(255), " +
                "hash_algorithm VARCHAR(50), " +
                "status VARCHAR(50)" +
                ")");

        // Purge any stale artifacts to maintain clear, predictable tracking parameters
        jdbcTemplate.execute("TRUNCATE TABLE audit_log");

        // Insert a unique validation signature corresponding to your project metadata
        String insertSql = "INSERT INTO audit_log (owner, action, hash_algorithm, status) " +
                           "VALUES ('Talia McCarthy-Wielenga', 'GENERATE_HASH', 'SHA-256', 'SUCCESS')";
        jdbcTemplate.execute(insertSql);

        // Verify that the record is present in memory before the context shutdown occurs
        List<Map<String, Object>> records = jdbcTemplate.queryForList("SELECT * FROM audit_log");
        assertEquals(1, records.size(), "Audit record must be successfully written to local cache.");
        System.out.println(">>> STEP 1 COMPLETE: Audit record committed. Initiating severe context teardown/reboot simulation...");
    }

    /**
     * STEP 2: Verifies persistence following the application context restart.
     * When this test executes, Spring Boots boots up an entirely fresh context lifecycle.
     * If the database were in-memory, the table would be empty. Because it is file-backed,
     * the records are read back out of the physical storage disk.
     */
    @Test
    @Order(2)
    public void step2_verifyRecordSurvivedRestart() {
        System.out.println(">>> STEP 2 START: Fresh application context booted. Validating file-backed storage arrays...");

        // Query the file-backed tables inside the fresh context instance
        List<Map<String, Object>> records = jdbcTemplate.queryForList("SELECT * FROM audit_log");

        // Assert that the record was successfully loaded out of the disk asset pathway
        assertFalse(records.isEmpty(), "CRITICAL FAILURE: Audit logs were wiped during context reboot. H2 is not file-backed!");
        assertEquals(1, records.size(), "Database must retain exactly 1 record following the system restart sequence.");

        // Validate the specific contents to guarantee complete data block preservation
        Map<String, Object> persistentRecord = records.get(0);
        assertEquals("Talia McCarthy-Wielenga", persistentRecord.get("owner"));
        assertEquals("SHA-256", persistentRecord.get("hash_algorithm"));
        assertEquals("SUCCESS", persistentRecord.get("status"));

        System.out.println(">>> SUCCESS EVIDENCE DETECTED: Audit record successfully survived application reboot! Persistent parameters validated.");
    }
}
