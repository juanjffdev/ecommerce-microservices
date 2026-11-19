package com.solenia.auth_service.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String saludo(String request) throws Exception{

        String message;

        switch (request){
            case "1":
                message ="Seleccionaste el primer mensaje";
                break;
            case "2":
                message="Seleccionaste el segundo mensaje";
                break;
            default:
                throw new Exception("Hola los kp");
        }



        return message;
    }
}
