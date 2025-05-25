package com.lenin.backend.gestionusuarios.controller;

import com.lenin.backend.gestionusuarios.model.Usuario;
import com.lenin.backend.gestionusuarios.repository.UsuarioRepository;
import com.lenin.backend.gestionusuarios.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> datos) {
        String correo = datos.get("correo");
        String password = datos.get("password");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());
        return ResponseEntity.ok(Map.of("token", token, "usuario", usuario));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> listar(HttpServletRequest request) {
        String rol = (String) request.getAttribute("rol");
        if (!"Admin".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(403).body(Map.of("error", "Acceso denegado"));
        }

        return ResponseEntity.ok(usuarioRepository.findAll());
    }
}
