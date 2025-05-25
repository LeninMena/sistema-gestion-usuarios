package com.lenin.backend.gestionusuarios.controller;

import com.lenin.backend.gestionusuarios.dto.LoginRequest;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(request.getCorreo());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }

        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }

        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol());
        return ResponseEntity.ok(Map.of(
            "token", token,
            "usuario", usuario
        ));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> listar(HttpServletRequest request) {
        String rol = (String) request.getAttribute("rol");
        if (!"Admin".equalsIgnoreCase(rol)) {
            return ResponseEntity.status(403).body(Map.of("error", "Acceso denegado"));
        }
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario nuevo, HttpServletRequest request) {
        String rol = (String) request.getAttribute("rol");
        if (rol == null || !rol.equalsIgnoreCase("Admin")) {
            return ResponseEntity.status(403).body(Map.of("error", "Acceso denegado"));
        }

        if (usuarioRepository.findByCorreo(nuevo.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Correo ya registrado"));
        }

        nuevo.setId(0); // ignorar si lo mandan desde Swagger
        nuevo.setPassword(passwordEncoder.encode(nuevo.getPassword()));
        usuarioRepository.save(nuevo);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));
    }
}
