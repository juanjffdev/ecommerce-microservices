package com.solenia.auth_service.application.mapper;

import com.solenia.auth_service.application.dto.RegisterRequest;
import com.solenia.auth_service.application.dto.UserResponse;
import com.solenia.auth_service.domain.model.User;


public class UserMapper {

    public UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public User toUser(RegisterRequest request){
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole("USER");
        return user;
    }
}
