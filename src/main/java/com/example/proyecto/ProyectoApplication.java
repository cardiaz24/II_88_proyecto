package com.example.proyecto;

import com.example.proyecto.model.Rol;
import com.example.proyecto.model.Usuario;
import com.example.proyecto.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProyectoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoApplication.class, args);
    }

    @Bean
    CommandLineRunner seedUsuarios(UsuarioRepository usuarios, PasswordEncoder encoder) {
        return args -> {
            if (!usuarios.existsByUsername("admin")) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setApellidos("Del Sistema");
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setEmail("admin@biblioteca.com");
                admin.setRol(Rol.ADMIN);
                admin.setEnabled(true);
                usuarios.save(admin);
                System.out.println("Usuario ADMIN creado: admin / admin123");
            }

            if (!usuarios.existsByUsername("user")) {
                Usuario user = new Usuario();
                user.setNombre("Usuario");
                user.setApellidos("Normal");
                user.setUsername("user");
                user.setPassword(encoder.encode("user123"));
                user.setEmail("user@biblioteca.com");
                user.setRol(Rol.USER);
                user.setEnabled(true);
                usuarios.save(user);
                System.out.println("Usuario USER creado: user / user123");
            }
        };
    }
}