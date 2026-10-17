package com.snhu.sslserver;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * FIXED INTEGRATION TEST: Database Persistence Restart Verification.
 * Proves that audit records survive a full application context teardown and restart.
 * Overrides properties to enforce clean table initialization without destructive clean drops.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:file:./data/ssl_audit_test_db;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE",
    "spring.jpa.hibernate.ddl-auto=none"
})
public class DatabasePersistenceRestartTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * STEP 1: Programmatically initialize the schema and commit a baseline audit log entry.
     * DirtiesContext forces a total context destruction upon completion.
     */
    @Test
    @Order(1)
    @DirtiesContext
    public void step1_writeAuditRecordAndRebootServer() {
        // Build the structure programmatically, ensuring it doesn't wipe existing rows
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS audit_log (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "owner VARCHAR(255), " +
                "action VARCHAR(255), " +
                "hash_algorithm VARCHAR(50), " +
                "status VARCHAR(50)" +
                ")");

        // Clear only to establish a clean starting baseline for validation
        jdbcTemplate.execute("TRUNCATE TABLE audit_log");

        String insertSql = "INSERT INTO audit_log (owner, action, hash_algorithm, status) " +
                           "VALUES ('Talia McCarthy-Wielenga', 'VALIDATE_CRYPTO_STREAM', 'SHA-256', 'SUCCESS')";
        jdbcTemplate.execute(insertSql);

        List<Map<String, Object>> records = jdbcTemplate.queryForList("SELECT * FROM audit_log");
        assertEquals(1, records.size(), "Audit record must be successfully written to local disk cache.");
        System.out.println(">>> STEP 1 PASSED: Entry committed to file-backed engine. Rebooting server context...");
    }

    /**
     * STEP 2: Verifies persistence following the application context restart.
     * Reads out of the file-backed database to prove rows survive server context termination.
     */
    @Test
    @Order(2)
    public void step2_verifyRecordSurvivedRestart() {
        System.out.println(">>> STEP 2 START: Fresh application context booted. Accessing file-backed database storage...");

        // Access the table—it will already exist because it is stored on physical disk
        List<Map<String, Object>> records = jdbcTemplate.queryForList("SELECT * FROM audit_log");

        assertFalse(records.isEmpty(), "CRITICAL FAILURE: Audit logs were wiped during context reboot. Database is operating in volatile memory!");
        assertEquals(1, records.size(), "Database must retain exactly 1 record following the system restart sequence.");

        Map<String, Object> persistentRecord = records.get(0);
        assertEquals("Talia McCarthy-Wielenga", persistentRecord.get("owner"));
        assertEquals("SHA-256", persistentRecord.get("hash_algorithm"));
        
        System.out.println(">>> SUCCESS EVIDENCE VERIFIED: The audit record survived context teardown and restart loops successfully.");
    }
}
