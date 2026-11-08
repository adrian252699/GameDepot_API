package com.example.GameDepotAPI.service;

import com.example.GameDepotAPI.dto.enums.EstadoOrden;
import com.example.GameDepotAPI.dto.enums.EstadoPago;
import com.example.GameDepotAPI.dto.enums.MetodoDePago;
import com.example.GameDepotAPI.dto.pago.CrearPagoResponseDTO;
import com.example.GameDepotAPI.exception.NegocioException;
import com.example.GameDepotAPI.exception.RecursoNoEncontradoException;
import com.example.GameDepotAPI.model.orden.Orden;
import com.example.GameDepotAPI.model.pago.Pago;
import com.example.GameDepotAPI.repository.OrdenRepository;
import com.example.GameDepotAPI.repository.PagoRepository;
import com.example.GameDepotAPI.repository.VideojuegoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PagoService {
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private VideojuegoRepository videojuegoRepository;

    /* Para implementar mercadoPago
    @Value("${mercadopago.access-token}")
    private String mercadoPagoAccessToken;

    @Value("${app.frontend.cliente.url}")
    private String frontendClienteUrl;

    @Value("${app.api.url}")
    private String apiUrl;
    */

    @Transactional
    public CrearPagoResponseDTO crearPago(Long ordenId){
        Orden orden = ordenRepository.findById(ordenId).orElseThrow(
                ()-> new RecursoNoEncontradoException("Orden no encontrada")
        );

        if (orden.getEstadoPago() == EstadoPago.APROBADO) {
            throw new NegocioException("La orden ya fue pagada");
        }

        if (orden.getTotal() == null || orden.getTotal().compareTo(BigDecimal.ZERO) <= 0){
            throw new NegocioException("El total de la orden debe ser mayor a 0");
        }

        Pago pago = pagoRepository.findByOrdenId(orden.getId()).orElse(new Pago());

        pago.setOrden(orden);
        pago.setEstadoPago(EstadoPago.PENDIENTE);
        pago.setMonto(orden.getTotal());
        pago.setFecha(LocalDateTime.now());

        /*Por ahora solo se admite pago con tarjeta*/
        pago.setMetodoDePago(MetodoDePago.TARJETA);

        pagoRepository.save(pago);

        orden.setEstadoPago(EstadoPago.PENDIENTE);
        orden.setEstado(EstadoOrden.PROCESANDO);
        ordenRepository.save(orden);

        return new CrearPagoResponseDTO(
                orden.getId(),
                null,
                null
        );
    }
}
