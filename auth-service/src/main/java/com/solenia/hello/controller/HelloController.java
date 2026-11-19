package com.solenia.auth_service.controller;


import com.solenia.auth_service.service.HelloService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/saludo")
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService){
        this.helloService= helloService;
    }

    @GetMapping("/{request}")
    public ResponseEntity<String> saludo(@PathVariable String request){
        String message;
        try {
            message = helloService.saludo(request);
        }
        catch (Exception exception){
            return ResponseEntity.ok(exception.getMessage());
        }
        return ResponseEntity.ok(message);
    }
}
