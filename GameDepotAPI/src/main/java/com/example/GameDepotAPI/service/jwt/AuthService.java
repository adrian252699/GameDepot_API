package com.example.GameDepotAPI.service.jwt;

import com.example.GameDepotAPI.dto.usuario.InicioSesionRequestDTO;
import com.example.GameDepotAPI.dto.usuario.MostrarUsuarioDTO;
import com.example.GameDepotAPI.dto.usuario.SesionResponseDTO;
import com.example.GameDepotAPI.model.usuario.Usuario;
import com.example.GameDepotAPI.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public SesionResponseDTO iniciarSesion(InicioSesionRequestDTO request) {
        String emailNormalizado = request.getEmail().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado).orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        boolean passwordCorrecta = passwordEncoder.matches(request.getPassword(), usuario.getPassword());

        if (!passwordCorrecta) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        String token = jwtService.generarToken(usuario);

        MostrarUsuarioDTO usuarioDTO =
                new MostrarUsuarioDTO(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getRol().toString(),
                        usuario.getFechaRegistro()
                );

        return new SesionResponseDTO(
                token,
                "Bearer",
                jwtService.obtenerExpiracionEnSegundos(),
                usuarioDTO
        );
    }
}
