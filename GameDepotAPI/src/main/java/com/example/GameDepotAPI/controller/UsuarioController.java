package com.example.GameDepotAPI.controller;

import com.example.GameDepotAPI.dto.usuario.ActualizarUsuarioDTO;
import com.example.GameDepotAPI.dto.usuario.MostrarUsuarioDTO;
import com.example.GameDepotAPI.dto.usuario.RegistrarUsuarioDTO;
import com.example.GameDepotAPI.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    /*Falta implementar la logica del login*/

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public MostrarUsuarioDTO obtenerUsuarioPorId(@PathVariable Long id){
        return usuarioService.obtenerPorId(id);
    }

    /*Tengo duda de que este endpoint pueda servirle al cliente*/
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
