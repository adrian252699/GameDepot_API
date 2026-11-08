package com.example.GameDepotAPI.dto.orden.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemOrdenDetalleResponseDTO {
    private Long videojuegoId;
    private String nombreVideojuego;
    private String categoria;
    private String imagenUrl;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
}
