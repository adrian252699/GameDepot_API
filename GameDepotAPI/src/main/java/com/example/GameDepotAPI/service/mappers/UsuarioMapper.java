package com.example.GameDepotAPI.service.mappers;

import com.example.GameDepotAPI.dto.enums.RolUsuario;
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
                usuario.getPais(),
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
                usuario.getFechaRegistro(),
                usuario.getPais()
        );
    }

    public static Usuario toUsuarioEntity(MostrarUsuarioDTO mostrarUsuarioDTO) {
        if (mostrarUsuarioDTO == null) {
            return null;
        }

        return new Usuario(
                mostrarUsuarioDTO.getId(),
                mostrarUsuarioDTO.getNombre(),
                mostrarUsuarioDTO.getEmail(),
                RolUsuario.valueOf(mostrarUsuarioDTO.getRol()),
                mostrarUsuarioDTO.getFechaRegistro(),
                mostrarUsuarioDTO.getPais()
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
