package com.example.GameDepotAPI.dto.carrito;

import com.example.GameDepotAPI.dto.item_carrito.ItemCarritoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarritoResponseDTO {
    private Long id;
    private List<ItemCarritoResponseDTO> items;
    private Integer cantidadItems;
    private Integer cantidadProductos;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;

}
