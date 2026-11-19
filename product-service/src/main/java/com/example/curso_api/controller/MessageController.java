package com.example.curso_api.controller;

import com.example.curso_api.entity.Message;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")

public class MessageController {
    private List<Message> mensajes = new ArrayList<>();

    public MessageController(){
        mensajes.add(new Message(1,"Hola dupla que tal?"));
        mensajes.add(new Message(2, "Que haces maquina"));
        mensajes.add(new Message(3,"Crackaso"));
    }

    @GetMapping
    public List<Message> listaMensajes(){
        return mensajes;
    }

    @GetMapping("/{id}")
    public Message obtenerMensajePorId(@PathVariable int id){
        Optional<Message> message = mensajes.stream()
                .filter(m -> m.getId()==id)
                .findFirst();
        return message.orElse(null);
    }
    @PostMapping
    public Message crearMesaje(@RequestBody Message message){
        mensajes.add(message);
        return message;
    }

    @DeleteMapping("{id}")
    public void eliminarMensaje(@PathVariable int id){
        mensajes.removeIf(m->m.getId()==id);
    }
}
