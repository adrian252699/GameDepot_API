package com.example.GameDepotAPI.service;

import com.example.GameDepotAPI.exception.NegocioException;
import com.example.GameDepotAPI.exception.RecursoNoEncontradoException;
import com.example.GameDepotAPI.model.videojuego.Videojuego;
import com.example.GameDepotAPI.repository.VideojuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VideojuegoService {

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    public Videojuego guardarVideojuego(Videojuego videojuego) {

        return videojuegoRepository.save(videojuego);
    }

    public Videojuego obtenerPorId(Long id) {
        return videojuegoRepository.getById(id);
    }

    public List<Videojuego> obtenerVideosjuegosActivos() {
        List<Videojuego> videojuegos = videojuegoRepository.findAll();

        return  videojuegos.stream().filter(v -> v.getActivo()==true).toList();
    }

    public Videojuego actualizarVideojuego(Long id, Videojuego videojuegoEditado) {
        return videojuegoRepository.findById(id).map(videojuego ->{
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
                videojuego.setCategoria(videojuegoEditado.getCategoria());
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

        }).orElseThrow(() -> new RecursoNoEncontradoException("Producto con ID " + id + " no encontrado."));
    }

    public void desactivarVideojuego(Long id) {
        Videojuego videojuego = obtenerPorId(id);
        videojuego.setActivo(false);
        videojuegoRepository.save(videojuego);
    }


    private void validarPrecio(BigDecimal precio) {
        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegocioException("El precio no puede ser negativo.");
        }
        if (precio.compareTo(new BigDecimal("999999.99")) > 0) {
            throw new NegocioException("El precio excede el valor máximo permitido.");
        }
    }


}
