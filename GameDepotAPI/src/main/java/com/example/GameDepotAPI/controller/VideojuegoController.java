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

    /*Un cliente solo tendra acceso visual a los videojuegos,
    * no puede crear o modificar videojuegos*/

    /*Aun falta agregar filtros de busqueda*/

    @Autowired
    private VideojuegoService videojuegoService;

    /*Este endpoint se utiliza para cargar la pagina principal*/
    @GetMapping()
    public List<MostrarVideojuegoDTO> listarVideojuegosActivos(){
        return videojuegoService.obtenerVideosjuegosActivos();
    }

    /*Este endpoint se utilizara al seleccionar un videojuego y mostrar su informacion*/
    @GetMapping("/{id}")
    public MostrarVideojuegoDTO obtenerVideojuegoPorId(@PathVariable Long id){
        return videojuegoService.obtenerPorId(id);
    }

    /* Este endpoint corresponde a un admin no a un cliente
    @PostMapping()
    public GuardarVideojuegoDTO crearVideojuego(@RequestBody GuardarVideojuegoDTO videojuego){
        return videojuegoService.guardarVideojuego(videojuego);
    }*/

    /* Este endpoint corresponde a un admin no a un cliente
    @PatchMapping("/{id}")
    public ActualizarVideojuegoDTO actualizarVideojuego(@PathVariable Long id, @RequestBody ActualizarVideojuegoDTO videojuego){
        return videojuegoService.actualizarVideojuego(id, videojuego);
    }*/

}
