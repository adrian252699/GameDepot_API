package com.example.GameDepotAPI.controller;

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
    public List<Videojuego> listarVideojuegosActivos(){
        return videojuegoService.obtenerVideosjuegosActivos();
    }

    @GetMapping("/{id}")
    public Videojuego obtenerVideojuegoPorId(@PathVariable Long id){
        return videojuegoService.obtenerPorId(id);
    }

    @PostMapping()
    public Videojuego crearVideojuego(@RequestBody Videojuego videojuego){
        return videojuegoService.guardarVideojuego(videojuego);
    }

    @PatchMapping("/{id}")
    public Videojuego actualizarVideojuego(@PathVariable Long id, @RequestBody Videojuego videojuego){
        return videojuegoService.actualizarVideojuego(id, videojuego);
    }

}
