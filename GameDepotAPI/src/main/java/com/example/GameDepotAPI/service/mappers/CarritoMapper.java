package com.example.GameDepotAPI.service.mappers;

import com.example.GameDepotAPI.dto.carrito.CarritoResponseDTO;
import com.example.GameDepotAPI.dto.item_carrito.ItemCarritoResponseDTO;
import com.example.GameDepotAPI.model.carrito.Carrito;
import com.example.GameDepotAPI.model.carrito.ItemCarrito;

import java.math.BigDecimal;
import java.util.List;

public class CarritoMapper {
    public static CarritoResponseDTO toResponseDTO(Carrito carrito) {
        List<ItemCarritoResponseDTO> itemsDTO =
                carrito.getItems()
                        .stream()
                        .map(CarritoMapper::toItemDTO)
                        .toList();

        int cantidadProductos =
                carrito.getItems()
                        .stream()
                        .mapToInt(ItemCarrito::getCantidad)
                        .sum();

        BigDecimal subtotal =
                itemsDTO.stream()
                        .map(ItemCarritoResponseDTO::getSubtotal)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal descuento = BigDecimal.ZERO;
        BigDecimal total = subtotal.subtract(descuento);

        return new CarritoResponseDTO(
                carrito.getId(),
                itemsDTO,
                itemsDTO.size(),
                cantidadProductos,
                subtotal,
                descuento,
                total

        );
    }

    private static ItemCarritoResponseDTO toItemDTO(ItemCarrito item) {
        BigDecimal precio = item.getVideojuego().getPrecio();

        BigDecimal subtotal = precio.multiply(
                BigDecimal.valueOf(item.getCantidad()));

        ItemCarritoResponseDTO dto = new ItemCarritoResponseDTO();

        dto.setId(item.getId());
        dto.setVideojuegoId(item.getVideojuego().getId());
        dto.setCategoria(item.getVideojuego().getCategoria().getNombre());
        dto.setNombreVideojuego(item.getVideojuego().getNombre());
        dto.setStockDisponible(item.getVideojuego().getStock());
        dto.setPrecioUnitario(precio);
        dto.setSubtotal(subtotal);
        dto.setCantidad(item.getCantidad());
        return dto;
    }
}
