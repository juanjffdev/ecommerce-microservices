package com.example.curso_api.dto;


import com.example.curso_api.entity.Categoria;
import com.example.curso_api.entity.EstadoProducto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CategoriaDTO {

    private Long idCategoria;

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Size(min = 3, max=50 , message="El nombre de la categoria debe tener entre 3 y 50 caracteres")
    private String nombreCategoria;
}
