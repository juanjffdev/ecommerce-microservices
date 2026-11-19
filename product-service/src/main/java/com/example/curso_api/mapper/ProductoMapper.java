package com.example.curso_api.mapper;

import com.example.curso_api.dto.ProductoDTO;
import com.example.curso_api.dto.ProductoPlanoDTO;
import com.example.curso_api.entity.Producto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Producto toEntity(ProductoDTO productoDTO){
        return modelMapper.map(productoDTO, Producto.class);
    }

    public void toEntity(ProductoDTO productoDTO,Producto productoExistente){
        modelMapper.map(productoDTO,productoExistente);
    }

    public ProductoDTO toDTO(Producto producto){
        return modelMapper.map(producto, ProductoDTO.class);
    }

    public ProductoPlanoDTO toPlanoDTO(Producto producto) {
        var categoria = producto.getCategoria();

        return new ProductoPlanoDTO(
                producto.getIdProducto(),
                producto.getNombreProducto(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getCantidad(),
                producto.getEstadoProducto().name(),
                categoria == null ? null : categoria.getIdCategoria(),
                categoria == null ? null : categoria.getNombreCategoria()
        );
    }
}
