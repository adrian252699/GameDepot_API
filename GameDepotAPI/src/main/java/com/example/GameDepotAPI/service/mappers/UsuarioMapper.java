package com.example.GameDepotAPI.service.mappers;

import com.example.GameDepotAPI.dto.usuario.ActualizarUsuarioDTO;
import com.example.GameDepotAPI.dto.usuario.MostrarUsuarioDTO;
import com.example.GameDepotAPI.dto.usuario.RegistrarUsuarioDTO;
import com.example.GameDepotAPI.model.usuario.Usuario;

public class UsuarioMapper {
    public static RegistrarUsuarioDTO toRegistrarUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new RegistrarUsuarioDTO(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().toString(),
                usuario.getFechaRegistro()
        );
    }

    public static MostrarUsuarioDTO toMostrarUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new MostrarUsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().toString(),
                usuario.getFechaRegistro()
        );
    }

    public static ActualizarUsuarioDTO toActualizarUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new ActualizarUsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getRol().toString(),
                usuario.getFechaRegistro()
        );
    }
}
