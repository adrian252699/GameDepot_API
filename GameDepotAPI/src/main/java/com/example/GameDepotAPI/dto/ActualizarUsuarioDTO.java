package com.example.GameDepotAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActualizarUsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private LocalDateTime fechaRegistro;
}
