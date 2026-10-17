package com.snhu.sslserver;

import java.security.MessageDigest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class SslServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SslServerApplication.class, args);
	}

}


@RestController

class ServerController {
    // A utility method to convert a byte array to a hexadecimal string.
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    @RequestMapping("/hash")
    public String myHash() throws NoSuchAlgorithmException {
        String data = "Hello World Check Sum! Talia McCarthy-Wielenga";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
        String hash = bytesToHex(hashBytes); 
        return "<p>data:" + data + "</p>" +
               "<p>Name of Cipher Algorithm Used: SHA-256" + "</p>" +
               "<p>Checksum Value: " + hash + "</p>";
    }

}
