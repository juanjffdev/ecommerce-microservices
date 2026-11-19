package com.example.curso_api.controller;


import com.example.curso_api.dto.CategoriaDTO;
import com.example.curso_api.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping()
    public ResponseEntity<CategoriaDTO> crearCategoria(@RequestBody CategoriaDTO categoria) throws Exception {
        CategoriaDTO nuevaCategoria = categoriaService.crearCategoria(categoria);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias (){
        List<CategoriaDTO> categorias= categoriaService.consultarCategorias();
        return new ResponseEntity<>(categorias,HttpStatus.OK);
    }
    @GetMapping("/{idCategoria}")
    public ResponseEntity<CategoriaDTO> obtenerCategoria(@PathVariable Long idCategoria)throws Exception{
        Optional<CategoriaDTO> categoriaOptional = categoriaService.obtenerCategoriaPorId(idCategoria);
        if(categoriaOptional.isPresent()){
            return  new ResponseEntity<>(categoriaOptional.get(),HttpStatus.OK);
        }else {
            throw new Exception("Categoria no encontrada");
        }
    }
    @PutMapping("/{idCategoria}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Long idCategoria,@RequestBody CategoriaDTO categoria){
        try {
            CategoriaDTO categoriaActualizada = categoriaService.actualizarCategoria(idCategoria,categoria);
            if (categoriaActualizada != null ){
                return new ResponseEntity<>(categoriaActualizada, HttpStatus.OK);
            }else{
                throw new Exception("Categoria no encontrada para actualizar");
            }
        }catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long idCategoria){
            categoriaService.eliminarCategoria(idCategoria);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
