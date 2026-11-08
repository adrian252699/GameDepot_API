package com.example.GameDepotAPI.controller;

import com.example.GameDepotAPI.dto.ActualizarUsuarioDTO;
import com.example.GameDepotAPI.dto.MostrarUsuarioDTO;
import com.example.GameDepotAPI.dto.RegistrarUsuarioDTO;
import com.example.GameDepotAPI.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public MostrarUsuarioDTO obtenerUsuarioPorId(@PathVariable Long id){
        return usuarioService.obtenerPorId(id);
    }

    @GetMapping()
    public MostrarUsuarioDTO obtenerUsuarioPorEmail(@RequestBody String email){
        return usuarioService.obtenerPorEmail(email);
    }

    @PostMapping()
    public RegistrarUsuarioDTO crearUsuario(@RequestBody RegistrarUsuarioDTO usuario){
        return usuarioService.registrarUsuario(usuario);
    }

    @PatchMapping("/{id}")
    public ActualizarUsuarioDTO actualizarUsuario(@PathVariable Long id, @RequestBody ActualizarUsuarioDTO usuario){
        return usuarioService.actualizarUsuario(id, usuario);
    }

}
