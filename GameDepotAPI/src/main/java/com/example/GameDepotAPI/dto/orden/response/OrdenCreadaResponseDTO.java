package com.example.GameDepotAPI.dto.orden.response;

import com.example.GameDepotAPI.dto.enums.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCreadaResponseDTO {
    private Long id;
    private String folio;
    private LocalDateTime fecha;
    private EstadoOrden estado;
    private BigDecimal subtotal;
    private BigDecimal total;
    private List<ItemOrdenResponseDTO> items;
}
