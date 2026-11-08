package com.example.GameDepotAPI.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrarUsuarioDTO {
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private String pais;
    private LocalDateTime fechaRegistro;

    public RegistrarUsuarioDTO(String nombre, String email, String rol, String pais, LocalDateTime fechaRegistro) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.pais = pais;
        this.fechaRegistro = fechaRegistro;
    }
}


