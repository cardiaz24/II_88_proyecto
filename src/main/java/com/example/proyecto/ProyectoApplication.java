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
				usuarios.save(new Usuario("admin", encoder.encode("admin123"), Rol.ADMIN));
				System.out.println("Usuario ADMIN creado: admin / admin123");
			}
			if (!usuarios.existsByUsername("user")) {
				usuarios.save(new Usuario("user", encoder.encode("user123"), Rol.USER));
				System.out.println(" Usuario USER creado: user / user123");
			}
		};
	}
}
