package com.example.curso_api.dto;
import com.example.curso_api.entity.Categoria;
import com.example.curso_api.entity.EstadoProducto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductoDTO {

    private Long idProducto;

    @NotBlank(message = "El nombre de producto no puede estar en blanco")
    @Size(max=100 , message="El nombre del producto no debe exceder los 100 caracteres")
    private String nombreProducto;

    @Size(max=255 , message="La descripción no debe exceder los 255 caracteres")
    private String descripcion;

    @NotNull(message= "El precio es obligatorio ")
    @DecimalMin(value = "0.0", inclusive = false,message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message= "La cantidad es obligatoria")
    @Min(value = 0,message = "La cantidad no puede ser negativa")
    private int cantidad;

    @NotNull(message = "El estado del producto es obligatorio")
    private EstadoProducto estadoProducto;

    private Categoria categoria;
}
