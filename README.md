# Secure Spring Boot Backend Service: Artemis Financial Re-Architecture
### Developed by: Talia McCarthy-Wielenga
### Focus Domain: Secure Backend Engineering & Healthcare Informatics

A production-grade, secure Spring Boot microservice demonstrating advanced software design, memory-efficient data streaming algorithms, and file-backed database persistence. Originally engineered as a coupled, single-file skeleton returning hardcoded signatures over basic HTTPS sockets, this repository details the end-to-end transformation of the **Artemis Financial Server** into a decoupled, highly auditable corporate asset capable of meeting the stringent security standards required in modern digital infrastructures.

---

## 🚀 Core Architectural Transformations

### 🛡️ 1. Software Design & Engineering (Separation of Concerns)
*   **The Problem:** The legacy backend application existed within a brittle, monolithic file layout (`SslServerApplication.java`), violating the Single Responsibility Principle by intertwining server configuration, web request routing, and cryptographic execution loops. Raw system exceptions were leaked over the open network, posing severe information disclosure threats.
*   **The Solution:** Deconstructed the monolith into an organized, multi-tiered MVC architecture under `com.snhu.sslserver`. Extracted web boundaries into an isolated `ChecksumController` and centralized a `GlobalExceptionHandler` interceptor layer to capture security exceptions. Raw, verbose server stack traces are caught internally, returning uniform, machine-readable JSON telemetry payloads instead of unformatted web text fragments.

### 🧮 2. Data Structures & Algorithms (Streaming & Strategy Framework)
*   **The Problem:** The original system relied on rigid, hardcoded string evaluation paths that forced whole payloads into active memory. Furthermore, it risked dangerous type-confusion by failing to separate hashing vectors from confidentiality ciphers.
*   **The Solution:** Implemented an adaptive **Strategy Pattern** creating a strict structural boundary between data integrity contracts (`HashStrategy`) and data confidentiality paths (`EncryptionStrategy`). To protect server memory allocations during high-frequency processing loops, data is processed sequentially using Java `InputStream` parameters and a balanced 1KB block buffer. 
*   **Cryptographic Hardening:** Addressed core structural fallback vulnerabilities by stripping provider-default cipher configurations—which risk defaulting to insecure, pattern-leaking Electronic Codebook (ECB) modes—and explicitly mandating an unyielding `AES/CBC/PKCS5Padding` cipher transformation. Low-level binary-to-hex utility conversions were refactored to prioritize computational speed and avoid object-instantiation overhead via bitwise mask shifts.

### 💾 3. Database Persistence (File-Backed Auditing & ORM Security)
*   **The Problem:** Cryptographic tracking logs and administrative audits were non-persistent, resulting in immediate data loss upon server shutdown or context connection termination.
*   **The Solution:** Migrated the backend configuration from volatile in-memory caching arrays (`jdbc:h2:mem:`) to a permanent, localized disk-backed relational system via a `jdbc:h2:file:` schema path. 
*   **SQL Injection Mitigation:** Integrated high-level Object-Relational Mapping (ORM) principles by configuring explicit `@Entity` mappings (`AuditLog`) and an abstraction interface layer extending `JpaRepository`. This abstraction layer leverages compile-time verified, parameterized queries to systematically eliminate SQL injection exploit paths.
*   **Durable Verification Proof:** Validated persistence durability metrics by engineering an automated `DatabasePersistenceRestartTest` harness. Utilizing Spring Boot context dirtying annotations, the test commits a transaction entry, forcefully terminates and reboots the active server context mid-execution, and programmatically asserts that the file asset successfully reloads from the disk storage arrays following the restart loop.

---

## 🧬 Professional Evolution Reflection
Transitioning from a pre-med biology background and an active role as a medical assistant into secure software engineering provided me with unique insights into the mission-critical nature of data durability. Working inside clinical environments exposed me directly to the severe operational disruptions caused by fragile software architectures and volatile electronic records tracking arrays. Overcoming the refactoring challenges in this repository bridged that clinical operational awareness with low-level software engineering, proving how strict design patterns, memory-optimized algorithms, and immutable database auditing trails safeguard enterprise data compliance and systemic reliability.

---

## ⚙️ Compilation and Build Verification

To execute and verify the secure server's build stability and run the automated persistence verification test loops, execute the following commands within your terminal environment:

```bash
# Clean previous targets and compile the complete multi-tiered package layout
mvn clean compile

# Execute the automated integration persistence restart test harness
mvn test -Dtest=DatabasePersistenceRestartTest

# Spin up the secure Spring Boot microservice context over port 8443
mvn spring-boot:run
```
