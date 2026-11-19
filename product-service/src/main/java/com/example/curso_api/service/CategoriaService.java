package com.example.curso_api.service;

import com.example.curso_api.dto.CategoriaDTO;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {


    CategoriaDTO crearCategoria(CategoriaDTO categoria);

    List<CategoriaDTO> consultarCategorias();

    CategoriaDTO actualizarCategoria(Long idCategoria,CategoriaDTO categoria);

    void eliminarCategoria(Long idCategoria);

    //FUNCIONALIDADES PARA EL MVP

    Optional<CategoriaDTO> obtenerCategoriaPorId(Long idCategoria);


}
