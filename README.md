# Professional ePortfolio: Computer Science Capstone
### Candidate: Talia McCarthy-Wielenga
### Specialization: Secure Backend Software Engineering & Healthcare Informatics
### Live Repository Deployment: https://github.com

---

## 🧬 Professional Self-Assessment

Your professional self-assessment covers your academic evolution from clinical healthcare (pre-med biology and medical assisting) to secure backend software engineering, emphasizing how real-world clinical data challenges drove your focus on data integrity and system resilience. It highlights your strategies for cross-functional communication, applying software engineering standards (such as decoupling a legacy monolith via an MVC layout and implementing the Strategy Pattern for secure cryptography), ensuring object-relational database persistence with H2 and JPA, and cultivating a proactive security mindset.

---

## 📹 Foundational Code Review
*   **Video Link Walkthrough:** [Access My Capstone Code Review Video Walkthrough Here][CS 499 Capstone Code Review Walkthrough on YouTube](https://youtu.be/OLAZJNas6II)

---

## 🚀 Technical Artifact Categories & Enhancements

### 🛡️ 1. Software Design & Engineering (Milestone Two)
*   **The Artifact:** Artemis Financial Secure Spring Boot Server microservice web layer routing engine.
*   **The Transformation:** Extracted a legacy monolithic configuration into a decoupled MVC architecture with explicit sub-packages and a centralized `GlobalExceptionHandler`.

### 🧮 2. Data Structures & Algorithms (Milestone Three)
*   **The Artifact:** Cryptographic domain processing layer and dynamic verification pipelines.
*   **The Transformation:** Integrated an object-oriented Strategy Pattern, optimized memory performance via Java `InputStreams`, and upgraded encryption standards to authenticated `AES/GCM/NoPadding`.

### 💾 3. Database Persistence (Milestone Four)
*   **The Artifact:** Compliance logging data layer and automated verification testing infrastructure.
*   **The Transformation:** Migrated from volatile in-memory caching to permanent H2 file-backed storage using JPA entity mappings and automated integration test harnesses to verify data durability.

---

## ⚙️ Compilation and Build Verification Loops
Run the following terminal commands to compile packages, execute integration tests, and run the server:

```powershell
\$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
.\mvnw compile
.\mvnw test -Dtest=DatabasePersistenceRestartTest
.\mvnw spring-boot:run
```
