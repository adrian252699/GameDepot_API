package com.example.GameDepotAPI.controller;

import com.example.GameDepotAPI.dto.usuario.InicioSesionRequestDTO;
import com.example.GameDepotAPI.dto.usuario.SesionResponseDTO;
import com.example.GameDepotAPI.service.jwt.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService service;


    @PostMapping
    public ResponseEntity<SesionResponseDTO> iniciarSesion(@Valid @RequestBody InicioSesionRequestDTO request) {
        SesionResponseDTO respuesta = service.iniciarSesion(request);
        return ResponseEntity.ok(respuesta);
    }
}
