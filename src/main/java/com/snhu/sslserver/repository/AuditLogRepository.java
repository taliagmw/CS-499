package com.snhu.sslserver.repository;

import com.snhu.sslserver.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DATABASE TIER: Data Access Object Interface Boundary.
 * Leverages Spring Data JPA to provide pre-compiled, safe database parameters.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
