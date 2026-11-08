package com.example.GameDepotAPI.service.mappers;

import com.example.GameDepotAPI.dto.usuario.ActualizarUsuarioDTO;
import com.example.GameDepotAPI.dto.usuario.RegistrarUsuarioDTO;
import com.example.GameDepotAPI.dto.videojuego.ActualizarVideojuegoDTO;
import com.example.GameDepotAPI.dto.videojuego.CategoriaDTO;
import com.example.GameDepotAPI.dto.videojuego.GuardarVideojuegoDTO;
import com.example.GameDepotAPI.dto.videojuego.MostrarVideojuegoDTO;
import com.example.GameDepotAPI.model.videojuego.Categoria;
import com.example.GameDepotAPI.model.videojuego.Videojuego;

import java.util.ArrayList;
import java.util.List;

public class VideojuegoMapper {

    public static List<MostrarVideojuegoDTO> toMostrarVideojuegoDTOList(List<Videojuego> videojuegos){
        List<MostrarVideojuegoDTO> mostrarVideojuegoDTO = new ArrayList<>();
        for (Videojuego videojuego : videojuegos) {
            mostrarVideojuegoDTO.add(toMostrarVideojuegoDTO(videojuego));
        }

        return mostrarVideojuegoDTO;
    }

    public static Videojuego toVideojuegoEntity(GuardarVideojuegoDTO videojuegoDTO){
        Videojuego videojuego = new Videojuego();

        videojuego.setNombre(videojuegoDTO.getNombre());
        videojuego.setDescripcion(videojuegoDTO.getDescripcion());
        videojuego.setPrecio(videojuegoDTO.getPrecio());
        videojuego.setCategoria(toCategoriaEntity(videojuegoDTO.getCategoria()));
        videojuego.setActivo(videojuegoDTO.getActivo());
        videojuego.setStock(videojuegoDTO.getStock());

        return videojuego;
    }

    public static GuardarVideojuegoDTO toGuardarVideojuegoDTO(Videojuego videojuego) {
        if (videojuego == null) {
            return null;
        }

        Categoria categoria = videojuego.getCategoria();

        CategoriaDTO categoriaDTO = new CategoriaDTO(videojuego.getCategoria().getId(),categoria.getNombre(),categoria.getDescripcion());

        return new GuardarVideojuegoDTO(
                videojuego.getNombre(),
                categoriaDTO,
                videojuego.getDescripcion(),
                videojuego.getPrecio(),
                videojuego.getStock(),
                videojuego.getActivo()
        );
    }

    public static MostrarVideojuegoDTO toMostrarVideojuegoDTO(Videojuego videojuego) {
        if (videojuego == null) {
            return null;
        }

        Categoria categoria = videojuego.getCategoria();

        CategoriaDTO categoriaDTO = new CategoriaDTO(videojuego.getCategoria().getId(),categoria.getNombre(),categoria.getDescripcion());

        return new MostrarVideojuegoDTO(
                videojuego.getId(),
                videojuego.getNombre(),
                categoriaDTO,
                videojuego.getDescripcion(),
                videojuego.getPrecio(),
                videojuego.getStock(),
                videojuego.getActivo()
        );
    }

    public static CategoriaDTO toCategoriaDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }

        return new CategoriaDTO(categoria.getId(), categoria.getNombre(),categoria.getDescripcion());
    }

    public static Categoria toCategoriaEntity(CategoriaDTO categoria) {
        if (categoria == null) {
            return null;
        }

        return new Categoria(categoria.getId(), categoria.getNombre(),categoria.getDescripcion());
    }

    public static ActualizarVideojuegoDTO toActualizarVideojuegoDTO(Videojuego videojuego) {
        if (videojuego == null) {
            return null;
        }

        Categoria categoria = videojuego.getCategoria();

        CategoriaDTO categoriaDTO = new CategoriaDTO(videojuego.getCategoria().getId(),categoria.getNombre(),categoria.getDescripcion());

        return new ActualizarVideojuegoDTO(
                videojuego.getId(),
                videojuego.getNombre(),
                categoriaDTO,
                videojuego.getDescripcion(),
                videojuego.getPrecio(),
                videojuego.getStock(),
                videojuego.getActivo()
        );
    }
}
