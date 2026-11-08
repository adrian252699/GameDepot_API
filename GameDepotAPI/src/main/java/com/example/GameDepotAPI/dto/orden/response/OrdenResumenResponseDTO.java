package com.example.GameDepotAPI.dto.orden.response;

import com.example.GameDepotAPI.dto.enums.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenResumenResponseDTO {
    private Long id;
    private String folio;
    private LocalDateTime fecha;
    private EstadoOrden estado;
    private BigDecimal total;
    private Integer cantidadProductos;
    private String productoPrincipal;
    private Integer productosAdicionales;
}
