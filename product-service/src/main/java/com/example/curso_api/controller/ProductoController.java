package com.example.curso_api.controller;

import com.example.curso_api.dto.PageResponse;
import com.example.curso_api.dto.ProductoDTO;
import com.example.curso_api.dto.ProductoPlanoDTO;
import com.example.curso_api.entity.EstadoProducto;
import com.example.curso_api.entity.Producto;
import com.example.curso_api.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")

public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @PostMapping("/registrar2/{idCategoria}")
    public ResponseEntity<?> registrarProducto2(
            @PathVariable(required = true) Long idCategoria,
            @RequestParam("nombreProducto") String nombreProducto,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("cantidad") int cantidad,
            @RequestParam("estado") EstadoProducto estado) throws Exception {

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombreProducto(nombreProducto);
        productoDTO.setDescripcion(descripcion);
        productoDTO.setCantidad(cantidad);
        productoDTO.setPrecio(precio);
        productoDTO.setEstadoProducto(estado);

        ProductoDTO productoDDBB = productoService.crearProducto(idCategoria,productoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoDDBB);
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos(){
        List<ProductoDTO> productosDTO = productoService.consultarProductos();
        return ResponseEntity.ok(productosDTO);
    }

    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre){
        Optional<ProductoDTO> productoDTO = productoService.buscarPorNombre(nombre);
        return productoDTO.isPresent() ? ResponseEntity.ok(productoDTO.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto No Encontrado por nombre");
    }

    @GetMapping("/buscar/id/{idProducto}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long idProducto){
        Optional<ProductoDTO> productoDTO = productoService.consultarProductosPorId(idProducto);
        return productoDTO.isPresent() ? ResponseEntity.ok(productoDTO.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto No Encontrado");
    }

    @GetMapping("/buscar/categoria/{nombreCategoria}")
    public ResponseEntity<List<ProductoDTO>>consultarPorCategoria(@PathVariable String nombreCategoria){
        return ResponseEntity.ok(productoService.consultarProductoPorCategoria(nombreCategoria));

    }

    @PutMapping("/actualizar/{idProducto}")
    public ResponseEntity<?> actualizarPorId(
            @PathVariable Long idProducto,
            @RequestBody ProductoDTO producto
    ){
            return ResponseEntity.ok(
                    productoService.actualizarProducto(idProducto,producto)
            );
    }

    @DeleteMapping("/eliminar/{idProducto}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long idProducto){
        try {
            productoService.eliminarProducto(idProducto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/actualizar/estado/{idProducto}")
    public ResponseEntity<?> cambiarEstadoProducto(@PathVariable Long idProducto, @RequestBody EstadoProducto estadoProducto){
        try {
            ProductoDTO productoActualizado = productoService.cambiarEstadoProducto(idProducto,estadoProducto);
            return ResponseEntity.ok(productoActualizado);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/estado/{estadoProducto}")
    public ResponseEntity<List<ProductoDTO>> listarProductosPorEstados (@PathVariable EstadoProducto estadoProducto){
        List<ProductoDTO> productosDTO = productoService.obtenerProductosPorEstado(estadoProducto);
        return ResponseEntity.ok(productosDTO);
    }

    @GetMapping("/paginacion")
    public ResponseEntity<PageResponse<ProductoPlanoDTO>> consultarPorPaginacion(
                @RequestParam(required = true) int page,
                @RequestParam(required = true) int size
    ){
        return ResponseEntity.ok(productoService.consultarPorPaginación(page,size));
    }


}
