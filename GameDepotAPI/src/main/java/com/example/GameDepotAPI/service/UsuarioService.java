package com.example.GameDepotAPI.service;

import com.example.GameDepotAPI.dto.usuario.ActualizarUsuarioDTO;
import com.example.GameDepotAPI.dto.usuario.MostrarUsuarioDTO;
import com.example.GameDepotAPI.dto.usuario.RegistrarUsuarioDTO;
import com.example.GameDepotAPI.dto.enums.RolUsuario;
import com.example.GameDepotAPI.exception.NegocioException;
import com.example.GameDepotAPI.exception.RecursoNoEncontradoException;
import com.example.GameDepotAPI.model.usuario.Usuario;
import com.example.GameDepotAPI.repository.UsuarioRepository;
import com.example.GameDepotAPI.service.mappers.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class UsuarioService {
    // Mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un símbolo
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Autowired
    private UsuarioRepository usuarioRepository;


    private final PasswordEncoder passwordEncoder;

    public UsuarioService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public MostrarUsuarioDTO obtenerPorId(Long id) {
        return UsuarioMapper.toMostrarUsuarioDTO(usuarioRepository.findById(id).orElseThrow(
                    () -> new RecursoNoEncontradoException("Usuario con ID " + id + " no encontrado.")
                )
        );
    }

    public MostrarUsuarioDTO obtenerPorEmail(String email) {
        return UsuarioMapper.toMostrarUsuarioDTO(usuarioRepository.findByEmail(email).orElse(null));
    }

    public RegistrarUsuarioDTO registrarUsuario(RegistrarUsuarioDTO usuario) {
        if (usuario == null) {
            throw new NegocioException("Usuario Vacio.");
        }

        if (usuario.getNombre() == null) {
            throw new NegocioException("Nombre Vacio.");
        }

        String nombreNormalizado = usuario.getNombre().trim();


        validarEmail(usuario.getEmail());
        validarPassword(usuario.getPassword());

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new NegocioException(
                    "Ya existe un usuario con el correo: "
                            + usuario.getEmail()
            );
        }

        Usuario usuarioGuardar = new Usuario();

        usuarioGuardar.setNombre(nombreNormalizado);
        usuarioGuardar.setEmail(usuario.getEmail());

        usuarioGuardar.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuarioGuardar.setRol(RolUsuario.CLIENTE);

        usuarioGuardar.setPais(usuario.getPais());
        usuarioGuardar.setFechaRegistro(LocalDateTime.now());

        usuarioRepository.save(usuarioGuardar);

        return UsuarioMapper.toRegistrarUsuarioDTO(usuarioGuardar);

    }

    public ActualizarUsuarioDTO actualizarUsuario(Long id,ActualizarUsuarioDTO usuarioParcial) {
        if (id == null || id <= 0) {
            throw new NegocioException("El ID del usuario debe ser un número positivo.");
        }
        if (usuarioParcial == null) {
            throw new NegocioException("Los datos del usuario no pueden ser nulos.");
        }

        return UsuarioMapper.toActualizarUsuarioDTO(usuarioRepository.findById(id).map(usuario -> {

            if (usuarioParcial.getNombre() != null) {
                String nuevoNombre = usuarioParcial.getNombre().trim();
                if (nuevoNombre.isEmpty()) {
                    throw new NegocioException("El nombre del usuario no puede estar vacío.");
                }
                if (nuevoNombre.length() > 100) {
                    throw new NegocioException("El nombre no puede superar los 100 caracteres.");
                }
                usuario.setNombre(nuevoNombre);
            }


            if (usuarioParcial.getEmail() != null) {
                String nuevoEmail = usuarioParcial.getEmail().trim().toLowerCase();

                validarEmail(nuevoEmail);

                if (!nuevoEmail.equals(usuario.getEmail()) && usuarioRepository.existsByEmail(nuevoEmail)) {
                    throw new NegocioException("Ya existe un usuario con el email: " + nuevoEmail);
                }
                usuario.setEmail(nuevoEmail);


            }

            if (usuarioParcial.getPassword() != null) {
                String nuevoPassword = usuarioParcial.getPassword().trim();

                validarPassword(nuevoPassword);
                if (passwordEncoder.matches(nuevoPassword, usuario.getPassword())) {
                    throw new NegocioException("La nueva contraseña no puede ser igual a la contraseña actual.");
                }
                usuario.setPassword(passwordEncoder.encode(nuevoPassword));
            }

            if (usuarioParcial.getRol() != null) {
                usuario.setRol(RolUsuario.valueOf(usuarioParcial.getRol()));
            }

            return usuarioRepository.save(usuario);

        }).orElseThrow(() -> new RecursoNoEncontradoException("Usuario con ID " + id + " no encontrado.")));

    }




    /*Metodos privados para validar*/
    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new NegocioException("El email del usuario es obligatorio.");
        }


        if (!EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches()) {
            throw new NegocioException("El formato del email no es válido.");
        }
    }

    private void validarPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new NegocioException("La contraseña del usuario es obligatoria.");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new NegocioException("La contraseña debe tener al menos 8 caracteres e incluir una mayúscula, una minúscula, un número y un símbolo especial.");
        }
    }
}
