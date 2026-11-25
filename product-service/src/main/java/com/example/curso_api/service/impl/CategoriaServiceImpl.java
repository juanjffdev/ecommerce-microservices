package com.example.curso_api.service.impl;

import com.example.curso_api.dto.CategoriaDTO;
import com.example.curso_api.entity.Categoria;
import com.example.curso_api.exception.BadRequestException;
import com.example.curso_api.exception.ConflictException;
import com.example.curso_api.exception.ResourceNotFoundException;
import com.example.curso_api.mapper.CategoriaMapper;
import com.example.curso_api.repository.CategoriaRepository;
import com.example.curso_api.repository.ProductoRepository;
import com.example.curso_api.service.CategoriaService;
import com.solenia.commons.logging.AppLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Override
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO){
        if(categoriaRepository.existsByNombreCategoria(categoriaDTO.getNombreCategoria())){
            AppLogger.debug(log,"CATEGORY_ALREADY_EXISTS");
            throw new BadRequestException("Ya existe una categoria con ese nombre");
        }
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        Categoria nuevaCategoria = categoriaRepository.save(categoria);
        AppLogger.info(log,"CATEGORY_CREATED","Categoría "+nuevaCategoria.getNombreCategoria()+" creada");
        return categoriaMapper.toDTO(nuevaCategoria);
    }

    @Override
    public List<CategoriaDTO> consultarCategorias() {
        List<Categoria> categorias =categoriaRepository.findAll();
        AppLogger.info(log,"CATEGORY_LIST_OBTAINED","Lista de categorias obtenida");
        return categorias.stream()
                .map(categoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoriaDTO> obtenerCategoriaPorId(Long idCategoria) {
        Optional<Categoria> categoria = categoriaRepository.findById(idCategoria);
        log.info("Categoria con id {} encontrada",idCategoria);
        return categoria.map(categoriaMapper::toDTO);
    }

    @Override
    public CategoriaDTO actualizarCategoria(Long idCategoria, CategoriaDTO categoria) {
        Categoria categoariaExistente = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> {
                    AppLogger.debug(log,"CATEGORY_NOT_FOUND_BY_ID");
                    return new ResourceNotFoundException("Categoria no encontrada");
                });
        categoariaExistente.setNombreCategoria(categoria.getNombreCategoria());
        Categoria categoriaActualizada = categoriaRepository.save(categoariaExistente);
        AppLogger.info(log,"CATEGORY_UPDATED","Categoria con id "+idCategoria+" actualizada a "+ categoriaActualizada.getNombreCategoria());
        return categoriaMapper.toDTO(categoariaExistente);
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long idCategoria) {

        Categoria categoriaExistente = categoriaRepository.findById(idCategoria)
                .orElseThrow(()->{
                    AppLogger.debug(log,"CATEGORY_NO_EXISTS");
                    return new ResourceNotFoundException("Categoria no encontrada");
                });

        if(productoRepository.existsByCategoria_idCategoria(idCategoria)){
            AppLogger.debug(log,"CATEGORY_DELETE_CONFLICT","No se puede eliminar la categoría, ya que existen productos asociados");
            throw new ConflictException("No se puede eliminar porque existen productos asociados a esta categoria");
        }

        categoriaRepository.deleteById(idCategoria);
        log.info("Categoria con id {} eliminada.",idCategoria);
    }

}
