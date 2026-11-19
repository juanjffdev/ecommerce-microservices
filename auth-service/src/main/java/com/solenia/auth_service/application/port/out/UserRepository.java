package com.solenia.auth_service.application.port.out;

import com.solenia.auth_service.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    long save(User user);

    int update(User user);
}
