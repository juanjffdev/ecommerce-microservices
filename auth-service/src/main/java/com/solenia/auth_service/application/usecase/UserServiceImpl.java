package com.solenia.auth_service.application.usecase;

import com.solenia.auth_service.application.dto.RegisterRequest;
import com.solenia.auth_service.application.dto.UserResponse;
import com.solenia.auth_service.application.mapper.UserMapper;
import com.solenia.auth_service.application.port.in.UserService;
import com.solenia.auth_service.application.port.out.PasswordHasher;
import com.solenia.auth_service.application.port.out.UserRepository;
import com.solenia.auth_service.domain.exception.EmailAlreadyExistException;
import com.solenia.auth_service.domain.exception.ResourceNotFoundException;
import com.solenia.auth_service.domain.model.User;
import com.solenia.commons.logging.AppLogger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

@Slf4j
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
            AppLogger.debug(log,"EMAIL_ALREADY_EXIST");
            throw new EmailAlreadyExistException();
        }

        //Desenvolvemos request a un objeto usuario y le asignamos un hash de contraseña
        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordHasher.hash(request.password()));

        //Persistimos, se espera como devolución el id
        newUser.setId(userRepository.save(newUser));
        AppLogger.info(log,"USER_LOGGED","Un usuario se ha logeado","User Name: "+ newUser.getName());
        return userMapper.toResponse(newUser);
    }






    @Override
    public List<UserResponse> listarUsuarios() {
        List<UserResponse> userList = userRepository.findAll().stream().map(userMapper::toResponse).toList();
        AppLogger.info(log,"USER_LIST","Lista de usuarios obtenida con éxito.");
        return userList;
    }



    @Override
    public Optional<UserResponse> buscarUsuarioPorId(Long id) {
        Optional<UserResponse> user = userRepository.findById(id).map(userMapper::toResponse);
        if(!user.isPresent()){
            AppLogger.info(log,"USER_FOUND_BY_ID","User id = " + id);
            throw new ResourceNotFoundException("No existe usuario con id: "+id);
        }

        return user;
    }

    @Override
    public Optional<UserResponse> buscarUsuarioPorEmail(String email) {
        Optional<UserResponse> user = userRepository.findByEmail(email).map(userMapper::toResponse);
        if(!user.isPresent()){
            AppLogger.info(log,"USER_FOUND_BY_EMAIL","User email = ", email);
            throw new ResourceNotFoundException("No existe usuario con email: "+email);
        }
        return user;
    }


}
