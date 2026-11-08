package com.example.GameDepotAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDateTime fechaRegistro;

    public RegistrarUsuarioDTO(String nombre, String email, String rol, LocalDateTime fechaRegistro) {
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
    }
}


