package com.example.curso_api.repository;

import com.example.curso_api.entity.EstadoProducto;
import com.example.curso_api.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {
    Optional<Producto> findByNombreProducto(String nombreProducto);

    Optional<Producto> findByIdProducto(Long idProducto);

    List<Producto> findByEstadoProducto(EstadoProducto estadoProducto);

    List<Producto> findByCategoria_NombreCategoriaIgnoreCase(String nombreCategoría);

    boolean existsByCategoria_idCategoria(Long idCategoria);
}
