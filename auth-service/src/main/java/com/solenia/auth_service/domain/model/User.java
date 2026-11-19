package com.solenia.auth_service.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class User {
    long id;

    String name;

    String email;

    String password;

    String role;

    LocalDateTime createdAt;
}
