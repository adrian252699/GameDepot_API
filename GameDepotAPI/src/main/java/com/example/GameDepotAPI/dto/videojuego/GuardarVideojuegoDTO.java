package com.example.GameDepotAPI.dto.videojuego;

import com.example.GameDepotAPI.model.videojuego.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuardarVideojuegoDTO {
    private String nombre;
    private CategoriaDTO categoria;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
}
