package com.solenia.auth_service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswordHash {

    public static void main(String[] args) {
        var encoder = new BCryptPasswordEncoder();

        String hash = encoder.encode("admin12345");

        System.out.println(hash);
    }
}