package com.solenia.auth_service.application.port.in;

import com.solenia.auth_service.application.dto.RegisterRequest;
import com.solenia.auth_service.application.dto.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponse registrarUsuario(RegisterRequest request);

    Optional<UserResponse> buscarUsuarioPorId(Long id);

    Optional<UserResponse> buscarUsuarioPorEmail(String email);

    List<UserResponse> listarUsuarios();

    //Optional<User> actualizarUsuario(Long id,UserDto userDto);

    //Optional<User> eliminarUsuario(Long id);

}
