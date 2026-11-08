package com.example.GameDepotAPI.controller;

import com.example.GameDepotAPI.dto.PaginacionDTO;
import com.example.GameDepotAPI.dto.enums.EstadoOrden;
import com.example.GameDepotAPI.dto.orden.response.OrdenCreadaResponseDTO;
import com.example.GameDepotAPI.dto.orden.response.OrdenDetalleResponseDTO;
import com.example.GameDepotAPI.dto.orden.response.OrdenResumenResponseDTO;
import com.example.GameDepotAPI.dto.usuario.MostrarUsuarioDTO;
import com.example.GameDepotAPI.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @PostMapping
    public ResponseEntity<OrdenCreadaResponseDTO> crearOrden(@AuthenticationPrincipal MostrarUsuarioDTO usuarioDTO){
        OrdenCreadaResponseDTO orden = ordenService.crearOrden(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }

    @GetMapping
    public ResponseEntity<PaginacionDTO<OrdenResumenResponseDTO>> listarOrdenes(@AuthenticationPrincipal MostrarUsuarioDTO usuarioDTO, @RequestParam(required = false) EstadoOrden estado, @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "10") int tamano){
        return ResponseEntity.ok(ordenService.listarOrdenes(usuarioDTO, estado, pagina, tamano));
    }

    @GetMapping("/pendiente")
    public ResponseEntity<OrdenCreadaResponseDTO> obtenerOrdenPendiente(@AuthenticationPrincipal MostrarUsuarioDTO usuario) {
        OrdenCreadaResponseDTO response = ordenService.obtenerOrdenPendientePago(usuario.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{ordenId}")
    public ResponseEntity<OrdenDetalleResponseDTO> obtenerDetalle(@AuthenticationPrincipal MostrarUsuarioDTO usuario, @PathVariable Long ordenId) {
        OrdenDetalleResponseDTO response = ordenService.obtenerDetalleOrden(usuario, ordenId);
        return ResponseEntity.ok(response);
    }
}
