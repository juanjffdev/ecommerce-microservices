package com.example.curso_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
Controller: Es una clase en java que gestiona las solicitudes http (Como get post put delete)
que llega a la aplicacion web o API, es el puente entre cliente y la logica del negocio
    -Es donde defines que hacer cuando alguien visita cierta URL

Controlador REST, es una clase en java que expone rutas HTTP (URLs)
    -Indica que esta clase es un controlador
    -Combina @Controller y @ResponseBody
    -No vistas HTML

**/

//@RestController// Indica que esta clase es un controlador REST
//@RequestMapping("micontroller")
public class SaludoController {
    @GetMapping("/saludo")
    public String saludar(){
        return "Hola kp";
    }

}
