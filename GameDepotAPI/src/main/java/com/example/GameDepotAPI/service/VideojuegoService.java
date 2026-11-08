package com.example.GameDepotAPI.service;

import com.example.GameDepotAPI.dto.videojuego.ActualizarVideojuegoDTO;
import com.example.GameDepotAPI.dto.videojuego.GuardarVideojuegoDTO;
import com.example.GameDepotAPI.dto.videojuego.MostrarVideojuegoDTO;
import com.example.GameDepotAPI.exception.NegocioException;
import com.example.GameDepotAPI.exception.RecursoNoEncontradoException;
import com.example.GameDepotAPI.model.videojuego.Videojuego;
import com.example.GameDepotAPI.repository.VideojuegoRepository;
import com.example.GameDepotAPI.service.mappers.VideojuegoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VideojuegoService {

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    /* Este endpoint corresponde a un admin
    public GuardarVideojuegoDTO guardarVideojuego(GuardarVideojuegoDTO videojuegoDTO) {

        Videojuego videojuegoEntity = videojuegoRepository.save(VideojuegoMapper.toVideojuegoEntity(videojuegoDTO));

        return VideojuegoMapper.toGuardarVideojuegoDTO(videojuegoEntity);
    }*/


    public MostrarVideojuegoDTO obtenerPorId(Long id) {
        return VideojuegoMapper.toMostrarVideojuegoDTO(videojuegoRepository.getById(id));
    }

    public List<MostrarVideojuegoDTO> obtenerVideosjuegosActivos() {
        List<Videojuego> videojuegos = videojuegoRepository.findAll();
        return  VideojuegoMapper.toMostrarVideojuegoDTOList(videojuegos.stream().filter(v -> v.getActivo()==true).toList());
    }

    /* este metodo corresponde a un admin no a un usuario
    public ActualizarVideojuegoDTO actualizarVideojuego(Long id, ActualizarVideojuegoDTO videojuegoEditado) {
        return VideojuegoMapper.toActualizarVideojuegoDTO(videojuegoRepository.findById(id).map(videojuego ->{
            if (videojuegoEditado.getNombre() != null) {

                String nuevoNombre = videojuegoEditado.getNombre().trim();

                if (nuevoNombre.isEmpty()) {
                    throw new NegocioException("El nombre del videojuego no puede estar vacío.");
                }
                if (nuevoNombre.length() > 155) {
                    throw new NegocioException("El nombre del videojuego no puede superar los 155 caracteres.");
                }

                videojuego.setNombre(nuevoNombre);
            }

            if (videojuegoEditado.getCategoria() != null) {
                videojuego.setCategoria(VideojuegoMapper.toCategoriaEntity(videojuegoEditado.getCategoria()));
            }

            if (videojuegoEditado.getActivo() != null) {
                videojuego.setActivo(videojuegoEditado.getActivo());
            }

            if (videojuegoEditado.getPrecio() != null) {
                validarPrecio(videojuegoEditado.getPrecio());
                videojuego.setPrecio(videojuegoEditado.getPrecio());
            }

            if (videojuegoEditado.getStock() != null) {
                if (videojuegoEditado.getStock() < 0) {
                    throw new NegocioException("El stock no puede ser negativo.");
                }
                videojuego.setStock(videojuegoEditado.getStock());
            }

            if (videojuegoEditado.getDescripcion() != null) {
                String nuevaDesc = videojuegoEditado.getDescripcion().trim();
                if (nuevaDesc.length() > 250) {
                    throw new NegocioException("La descripción no puede superar los 250 caracteres.");
                }
                videojuego.setDescripcion(nuevaDesc);
            }

            return videojuegoRepository.save(videojuego);

        }).orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado.")));
    }*/

    private void validarPrecio(BigDecimal precio) {
        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException("El precio no puede ser negativo.");
        }
        if (precio.compareTo(new BigDecimal("999999.99")) > 0) {
            throw new NegocioException("El precio excede el valor máximo permitido.");
        }
    }


}
