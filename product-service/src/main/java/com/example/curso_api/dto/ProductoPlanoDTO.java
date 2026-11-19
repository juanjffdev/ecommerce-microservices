package com.example.curso_api.dto;

public record ProductoPlanoDTO (
    Long id,
    String nombreProducto,
    String descripcion,
    Double precio,
    int cantidad,
    String estado,
    Long categoriaId,
    String nombreCategoria
){ }
