package com.snhu.sslserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Cleaned Core Main Application Boot Configuration.
 * Tightly coupled web components have been fully migrated out into separate domain packages.
 */
@SpringBootApplication
public class SslServerApplication {

    // Enforces the single responsibility mandate by restricting this class solely to initialization tasks
    public static void main(String[] args) {
        SpringApplication.run(SslServerApplication.class, args);
    }
}


