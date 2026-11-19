package com.solenia.auth_service.application.usecase;

import com.solenia.auth_service.application.dto.RegisterRequest;
import com.solenia.auth_service.application.dto.UserResponse;
import com.solenia.auth_service.application.mapper.UserMapper;
import com.solenia.auth_service.application.port.in.UserService;
import com.solenia.auth_service.application.port.out.PasswordHasher;
import com.solenia.auth_service.application.port.out.UserRepository;
import com.solenia.auth_service.domain.exception.EmailAlreadyExistException;
import com.solenia.auth_service.domain.model.User;

import java.util.List;
import java.util.Optional;

//@Service
public class UserServiceImpl implements UserService {
    //@Autowired// no es necesario si hacemos injeccion por constructor
    //para una sola dependencia.
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordHasher passwordHasher;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordHasher passwordHasher){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordHasher = passwordHasher;
    }



    @Override
    public UserResponse registrarUsuario(RegisterRequest request) {
        //Verficamos si ya existe un usuario con este email.
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new EmailAlreadyExistException();
        }

        //Desenvolvemos request a un objeto usuario y le asignamos un hash de contraseña
        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordHasher.hash(request.password()));

        //Persistimos, se espera como devolución el id
        newUser.setId(userRepository.save(newUser));

        return userMapper.toResponse(newUser);
    }






    @Override
    public List<UserResponse> listarUsuarios() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }



    @Override
    public Optional<UserResponse> buscarUsuarioPorId(Long id) {

        return userRepository.findById(id)
                .map(userMapper::toResponse);
    }

    @Override
    public Optional<UserResponse> buscarUsuarioPorEmail(String email) {

        return userRepository.findByEmail(email)
                .map(userMapper::toResponse);
    }


}
