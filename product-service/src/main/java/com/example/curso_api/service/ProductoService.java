package com.example.curso_api.service;

import com.example.curso_api.dto.PageResponse;
import com.example.curso_api.dto.ProductoDTO;
import com.example.curso_api.dto.ProductoPlanoDTO;
import com.example.curso_api.entity.EstadoProducto;
import com.example.curso_api.entity.Producto;

import java.util.Optional;
import java.util.List;

public interface ProductoService {

    ProductoDTO crearProducto(Long idCategoria, ProductoDTO producto);

    List<ProductoDTO> consultarProductos();

    Optional<ProductoDTO> consultarProductosPorId(Long idProducto);

    ProductoDTO actualizarProducto(Long idProducto,ProductoDTO producto);

    void eliminarProducto(Long idProducto)throws Exception;

    List<ProductoDTO> consultarProductoPorCategoria(String categoria);

    PageResponse<ProductoPlanoDTO> consultarPorPaginación(int page, int size);

    //HASTA ACÁ FUNCIONALIDADES REQUERIDAS PARA EL MVP






    Optional<ProductoDTO> buscarPorNombre(String nombre);

    ProductoDTO cambiarEstadoProducto();

    ProductoDTO cambiarEstadoProducto(Long idProducto,EstadoProducto nuevoEstadoProducto)throws Exception;

    List<ProductoDTO> obtenerProductosPorEstado(EstadoProducto estadoProducto);

}
