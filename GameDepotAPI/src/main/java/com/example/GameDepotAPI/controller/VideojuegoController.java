package com.example.GameDepotAPI.controller;

import com.example.GameDepotAPI.dto.videojuego.ActualizarVideojuegoDTO;
import com.example.GameDepotAPI.dto.videojuego.GuardarVideojuegoDTO;
import com.example.GameDepotAPI.dto.videojuego.MostrarVideojuegoDTO;
import com.example.GameDepotAPI.model.videojuego.Videojuego;
import com.example.GameDepotAPI.service.VideojuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/videojuego")
public class VideojuegoController {

    @Autowired
    private VideojuegoService videojuegoService;

    @GetMapping()
    public List<MostrarVideojuegoDTO> listarVideojuegosActivos(){
        return videojuegoService.obtenerVideosjuegosActivos();
    }

    @GetMapping("/{id}")
    public MostrarVideojuegoDTO obtenerVideojuegoPorId(@PathVariable Long id){
        return videojuegoService.obtenerPorId(id);
    }

    @PostMapping()
    public GuardarVideojuegoDTO crearVideojuego(@RequestBody GuardarVideojuegoDTO videojuego){
        return videojuegoService.guardarVideojuego(videojuego);
    }

    @PatchMapping("/{id}")
    public ActualizarVideojuegoDTO actualizarVideojuego(@PathVariable Long id, @RequestBody ActualizarVideojuegoDTO videojuego){
        return videojuegoService.actualizarVideojuego(id, videojuego);
    }

}
