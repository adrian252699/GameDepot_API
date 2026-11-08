package com.example.GameDepotAPI.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SesionResponseDTO {
    private String token;
    private String tipoToken;
    private long expiraEn;
    private MostrarUsuarioDTO usuario;
}
