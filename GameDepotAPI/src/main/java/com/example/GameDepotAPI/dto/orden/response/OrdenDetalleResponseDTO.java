package com.example.GameDepotAPI.dto.orden.response;

import com.example.GameDepotAPI.dto.enums.EstadoOrden;
import com.example.GameDepotAPI.dto.enums.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenDetalleResponseDTO {
    private Long id;
    private String folio;
    private LocalDateTime fecha;

    private EstadoOrden estado;
    private EstadoPago estadoPago;

    private String usuarioEmail;

    private List<ItemOrdenDetalleResponseDTO> items;

    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal costoEnvio;
    private BigDecimal total;

    private String metodoPago;
    /*

    private String preferenceId;
    private String mercadoPagoPaymentId;
     */
}
