package com.example.GameDepotAPI.controller;

import com.example.GameDepotAPI.dto.carrito.CarritoResponseDTO;
import com.example.GameDepotAPI.dto.item_carrito.AgregarItemCarritoRequestDTO;
import com.example.GameDepotAPI.dto.item_carrito.ModificarCantidadItemDTO;
import com.example.GameDepotAPI.dto.usuario.MostrarUsuarioDTO;
import com.example.GameDepotAPI.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public CarritoResponseDTO obtenerCarrito(@AuthenticationPrincipal MostrarUsuarioDTO usuarioDTO){
        return carritoService.obtenerCarrito(usuarioDTO.getId());
    }

    @PostMapping("/items")
    public CarritoResponseDTO agregarItem(@AuthenticationPrincipal MostrarUsuarioDTO usuarioDTO,@Valid @RequestBody AgregarItemCarritoRequestDTO request){
        return carritoService.agregarVideojuego(usuarioDTO,request);
    }

    @PatchMapping("/items/{itemId}")
    public CarritoResponseDTO actualizarCantidad(@AuthenticationPrincipal MostrarUsuarioDTO usuarioDTO, @PathVariable Long itemId, @Valid @RequestBody ModificarCantidadItemDTO modificacion){
        return carritoService.actualizarCantidad(usuarioDTO,itemId,modificacion.getCantidad());
    }

    @DeleteMapping("/items/{itemId}")
    public CarritoResponseDTO eliminarItem(@AuthenticationPrincipal MostrarUsuarioDTO usuarioDTO, @PathVariable Long itemId){
        return carritoService.eliminarItem(usuarioDTO,itemId);
    }
}
