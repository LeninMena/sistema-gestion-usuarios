// package com.lenin.backend.gestionusuarios.config;

// import com.lenin.backend.gestionusuarios.model.Usuario;
// import com.lenin.backend.gestionusuarios.repository.UsuarioRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration
// public class InitPasswordAdmin implements CommandLineRunner {

//     @Autowired
//     private UsuarioRepository usuarioRepository;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     @Override
//     public void run(String... args) throws Exception {
//         if (usuarioRepository.count() == 0) {
//             Usuario usuario = new Usuario();
//             usuario.setNombre("Super Admin");
//             usuario.setCorreo("superadmin@gmail.com");
//             usuario.setPassword(passwordEncoder.encode("admin123"));
//             usuario.setRol("Admin");

//             usuarioRepository.save(usuario);
//             System.out.println("✅ Usuario administrador creado correctamente.");
//         } else {
//             System.out.println("ℹ️ La tabla usuarios ya contiene datos. No se creó ningún usuario.");
//         }
//     }
// }
