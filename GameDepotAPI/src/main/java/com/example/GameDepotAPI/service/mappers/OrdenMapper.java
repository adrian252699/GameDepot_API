package com.example.GameDepotAPI.service.mappers;

import com.example.GameDepotAPI.dto.orden.response.*;
import com.example.GameDepotAPI.model.orden.ItemOrden;
import com.example.GameDepotAPI.model.orden.Orden;
import com.example.GameDepotAPI.model.videojuego.Videojuego;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrdenMapper {

    public static OrdenCreadaResponseDTO toCreadaResponseDTO(Orden orden) {
        List<ItemOrdenResponseDTO> items = orden.getItems()
                .stream()
                .map(OrdenMapper::toItemDTO)
                .toList();

        BigDecimal subtotal = items.stream().map(ItemOrdenResponseDTO::getSubtotal).reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        return new OrdenCreadaResponseDTO(
                orden.getId(),
                orden.getFolio(),
                orden.getFecha(),
                orden.getEstado(),
                subtotal,
                orden.getTotal(),
                items
        );
    }

    private static ItemOrdenResponseDTO toItemDTO(ItemOrden item) {
        BigDecimal subtotalBruto = item.getPrecioUnitario()
                .multiply(
                        BigDecimal.valueOf(
                                item.getCantidad()
                        )
                );

        BigDecimal subtotal = subtotalBruto.subtract(
                item.getDescuento()
        );

        return new ItemOrdenResponseDTO(
                item.getId(),
                item.getVideojuego().getId(),
                item.getVideojuego().getNombre(),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.getDescuento(),
                subtotal
        );
    }


    public static OrdenResumenResponseDTO toResumenResponseDTO(Orden orden) {
        int cantidadProductos = orden.getItems()
                .stream()
                .mapToInt(ItemOrden::getCantidad)
                .sum();

        String productoPrincipal = null;
        int productosAdicionales = 0;

        if (!orden.getItems().isEmpty()) {
            productoPrincipal = orden
                    .getItems()
                    .get(0)
                    .getVideojuego()
                    .getNombre();

            productosAdicionales =
                    orden.getItems().size() - 1;
        }

        return new OrdenResumenResponseDTO(
                orden.getId(),
                orden.getFolio(),
                orden.getFecha(),
                orden.getEstado(),
                orden.getTotal(),
                cantidadProductos,
                productoPrincipal,
                productosAdicionales
        );
    }

    public static OrdenDetalleResponseDTO toDetalleResponseDTO(Orden orden) {
        if (orden == null) {
            return null;
        }

        OrdenDetalleResponseDTO dto = new OrdenDetalleResponseDTO();

        dto.setId(orden.getId());
        dto.setFolio(orden.getFolio());
        dto.setFecha(orden.getFecha());
        dto.setEstado(orden.getEstado());
        dto.setEstadoPago(orden.getEstadoPago());


        if (orden.getUsuario() != null) {
            dto.setUsuarioEmail(
                    orden.getUsuario().getEmail()
            );
        }

        dto.setDescuento(
                BigDecimal.ZERO
        );

        dto.setSubtotal(
                calcularSubtotal(orden)
        );

        dto.setTotal(
                orden.getTotal() != null
                        ? orden.getTotal()
                        : BigDecimal.ZERO
        );


        dto.setItems(
                orden.getItems() == null
                        ? List.of()
                        : orden.getItems()
                        .stream()
                        .map(OrdenMapper::convertirItemDetalle)
                        .toList()
        );

        if (orden.getPago() != null) {
            dto.setMetodoPago(
                    orden.getPago().getMetodoDePago().toString()
            );

            /*Para implementar mercado pago
            dto.setPreferenceId(
                    orden.getPago().getPreferenceId()
            );

            dto.setMercadoPagoPaymentId(
                    orden.getPago().getMercadoPagoPaymentId()
            );*/
        }

        return dto;
    }

    private static ItemOrdenDetalleResponseDTO convertirItemDetalle(ItemOrden item) {
        ItemOrdenDetalleResponseDTO dto =
                new ItemOrdenDetalleResponseDTO();

        Videojuego videojuego = item.getVideojuego();

        if (videojuego != null) {
            dto.setVideojuegoId(
                    videojuego.getId()
            );

            dto.setNombreVideojuego(
                    videojuego.getNombre()
            );

            if (videojuego.getCategoria() != null) {
                dto.setCategoria(
                        videojuego.getCategoria().getNombre()
                );
            }
        }

        dto.setCantidad(
                item.getCantidad()
        );

        dto.setPrecioUnitario(
                item.getPrecioUnitario()
        );

        dto.setDescuento(
                item.getDescuento() != null
                        ? item.getDescuento()
                        : BigDecimal.ZERO
        );

        BigDecimal precio =
                item.getPrecioUnitario() != null
                        ? item.getPrecioUnitario()
                        : BigDecimal.ZERO;

        int cantidad =
                item.getCantidad() != null
                        ? item.getCantidad()
                        : 0;

        BigDecimal descuento =
                item.getDescuento() != null
                        ? item.getDescuento()
                        : BigDecimal.ZERO;

        dto.setSubtotal(
                precio.multiply(
                        BigDecimal.valueOf(cantidad)
                ).subtract(descuento)
        );

        return dto;
    }


    private static BigDecimal calcularSubtotal(Orden orden) {
        if (orden.getItems() == null
                || orden.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return orden.getItems()
                .stream()
                .map(item -> {
                    BigDecimal precio =
                            item.getPrecioUnitario() != null
                                    ? item.getPrecioUnitario()
                                    : BigDecimal.ZERO;

                    int cantidad =
                            item.getCantidad() != null
                                    ? item.getCantidad()
                                    : 0;

                    BigDecimal descuento =
                            item.getDescuento() != null
                                    ? item.getDescuento()
                                    : BigDecimal.ZERO;

                    return precio.multiply(
                            BigDecimal.valueOf(cantidad)
                    ).subtract(descuento);
                })
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}
