package com.example.curso_api.service.impl;

import com.example.curso_api.dto.PageResponse;
import com.example.curso_api.dto.ProductoDTO;
import com.example.curso_api.dto.ProductoPlanoDTO;
import com.example.curso_api.entity.Categoria;
import com.example.curso_api.entity.EstadoProducto;
import com.example.curso_api.entity.Producto;
import com.example.curso_api.exception.BadRequestException;
import com.example.curso_api.exception.ResourceNotFoundException;
import com.example.curso_api.mapper.ProductoMapper;
import com.example.curso_api.repository.CategoriaRepository;
import com.example.curso_api.repository.ProductoRepository;
import com.example.curso_api.service.ProductoService;
import com.solenia.commons.logging.AppLogger;
import com.solenia.commons.logging.LogResult;
import com.example.curso_api.logging.ProductLogEvent;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j // Me genera private static final Logger = LoggerFactory.getLogger(ProductoServiceImpl.class)
@Service
public class ProductServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @Override
    public ProductoDTO crearProducto(Long idCategoria, ProductoDTO productoDTO) {
        log.debug("Iniciando creación de product para Categoría ID = {}",idCategoria);
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(()->new ResourceNotFoundException("No existe categoria asociada a" + idCategoria));

        if(productoDTO.getPrecio()==null || productoDTO.getPrecio() <=0){
            throw new BadRequestException("El precio del producto debe ser mayor que 0");
        }

        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setCategoria(categoria);

        log.debug("Guardando producto, Categoría ID = {}",idCategoria);
        Producto productoGuardar = productoRepository.save(producto);
        log.info("Producto guardado. Producto id = {}",productoGuardar.getIdProducto());
        return productoMapper.toDTO(productoGuardar);
    }

    @Override
    public List<ProductoDTO> consultarProductos() {
        List<Producto> productos= productoRepository.findAll();
        List<ProductoDTO> response = productos.stream()
                .map(productoMapper::toDTO)
                .toList();

        return response;
    }

    @Override
    public Optional<ProductoDTO> consultarProductosPorId(Long idProducto) {
        Optional<Producto> producto=productoRepository.findByIdProducto(idProducto);
        log.info("Producto con id {} encontrado", idProducto);
        return producto.map(productoMapper::toDTO);
    }

    @Override
    public Optional<ProductoDTO> buscarPorNombre(String nombre) {
        Optional<Producto> producto=productoRepository.findByNombreProducto(nombre);
        log.info("Producto con nombre {} encontrado", nombre);
        return producto.map(productoMapper::toDTO);
    }


    @Override
    public List<ProductoDTO> obtenerProductosPorEstado(EstadoProducto estadoProducto) {
        List<Producto> productos = productoRepository.findByEstadoProducto(estadoProducto);
        log.info("Lista de productos con estado {} encontrados", estadoProducto);
        return productos.stream()
                .map(productoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoDTO> consultarProductoPorCategoria(String categoria) {

        return productoRepository
                .findByCategoria_NombreCategoriaIgnoreCase(categoria.trim())
                .stream()
                .map(productoMapper::toDTO)
                .toList();
    }



    @Override
    public PageResponse<ProductoPlanoDTO> consultarPorPaginación(int page, int size) {
        if (page < 0 || size < 1 || size >100){
            throw new BadRequestException(
                    "page debe ser >= 0 y size debe estar entre 1 y 100"
            );
        }

        var pageable = PageRequest.of(page,size, Sort.by("idProducto").ascending());

        var resultados = productoRepository.findAll(pageable);

        var contenido = resultados.getContent().stream()
                .map(productoMapper::toPlanoDTO)
                .toList();
        log.info("Productos encontrados");
        return new PageResponse<>(
                contenido,
                resultados.getNumber(),
                resultados.getSize(),
                resultados.getTotalElements(),
                resultados.getTotalPages()
        );
    }

    @Override
    @Transactional
    public ProductoDTO actualizarProducto(Long idProducto, ProductoDTO producto){
        Producto productoExistente= productoRepository.findByIdProducto(idProducto)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Product con ID "+idProducto+" no encontrado"
                        )
                );
        log.debug("Producto con id {} recuperado de la db",idProducto);
        productoExistente.setNombreProducto(producto.getNombreProducto());
        productoExistente.setDescripcion(producto.getDescripcion());
        productoExistente.setCantidad(producto.getCantidad());
        productoExistente.setEstadoProducto(producto.getEstadoProducto());
        productoExistente.setPrecio(producto.getPrecio());

        if(producto.getCategoria()==null || producto.getCategoria().getIdCategoria()==null) {
            throw new BadRequestException("El Id de categoría es obligatorio");
        }

        productoExistente.setCategoria(
                categoriaRepository.findById(
                        producto.getCategoria().getIdCategoria()
                ).orElseThrow(()->new ResourceNotFoundException(
                        "No existe categoria con id " + producto.getCategoria().getIdCategoria()
                )));

        Producto productoActualizado =productoRepository.save(productoExistente);
        log.info("Producto con id {} actualizado", idProducto);
        return productoMapper.toDTO(productoActualizado);
    }

    @Override
    public ProductoDTO cambiarEstadoProducto() {

        return null;
    }

    @Override
    public ProductoDTO cambiarEstadoProducto(Long idProducto, EstadoProducto nuevoEstadoProducto) throws Exception{
        Producto productoExistente= productoRepository.findByIdProducto(idProducto)
                .orElseThrow(()-> new Exception("Product con ID "+idProducto+" no encontrado"));
        productoExistente.setEstadoProducto(nuevoEstadoProducto);

        Producto productoActualizado = productoRepository.save(productoExistente);
        return productoMapper.toDTO(productoActualizado);
    }

    @Override
    public void eliminarProducto(Long idProducto) throws Exception{
        productoRepository.findByIdProducto(idProducto)
                .orElseThrow(()-> {
                    log.info("Producto con id {} no encontrado", idProducto);

                    return new Exception("Product con ID "+idProducto+" no encontrado");
                });
        productoRepository.deleteById(idProducto);
        log.info("Producto con id {} eliminado", idProducto);
    }
}
