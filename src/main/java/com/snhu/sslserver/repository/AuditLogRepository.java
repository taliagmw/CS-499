// File: src/main/java/com/snhu/sslserver/repository/AuditLogRepository.java
package com.snhu.sslserver.repository;

import com.snhu.sslserver.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
