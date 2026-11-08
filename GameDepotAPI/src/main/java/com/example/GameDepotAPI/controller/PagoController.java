package com.example.GameDepotAPI.controller;

import com.example.GameDepotAPI.dto.pago.CrearPagoRequestDTO;
import com.example.GameDepotAPI.dto.pago.CrearPagoResponseDTO;
import com.example.GameDepotAPI.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @PostMapping
    public ResponseEntity<CrearPagoResponseDTO> crearPago(@RequestBody CrearPagoRequestDTO request){
        CrearPagoResponseDTO response = pagoService.crearPago(request.getOrdenId());
        return ResponseEntity.ok(response);
    }
}
