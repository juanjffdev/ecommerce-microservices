package com.solenia.auth_service.adapter.web.controller;


import com.solenia.auth_service.application.dto.UserResponse;
import com.solenia.auth_service.application.port.in.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService){

        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listarUsuario(){
        List<UserResponse> list = userService.listarUsuarios();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> buscarPorId(@PathVariable Long id){
        //UserResponse userResponse = userService.buscarUsuarioPorId(id).get();
        //return ResponseEntity.ok(userResponse);
        return ResponseEntity.of(userService.buscarUsuarioPorId(id));
        // .of ES UNA ABREVIACIÓN DE.OK. Devuelve 201 OK + body Y 204 NOT_FOUND + null
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> buscarPorEmail(@PathVariable String email){
        //UserResponse userResponse = userService.buscarUsuarioPorEmail(email).get();
        //return ResponseEntity.ok(userResponse);
        return ResponseEntity.of(userService.buscarUsuarioPorEmail(email));
    }


}
