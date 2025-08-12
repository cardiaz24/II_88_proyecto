package com.example.proyecto;

import com.example.proyecto.model.*;
import com.example.proyecto.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class ProyectoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoApplication.class, args);
	}

	
  @Bean
  WebMvcConfigurer mvc() {
    return new WebMvcConfigurer() {
      @Override public void addViewControllers(ViewControllerRegistry r) {
        r.addRedirectViewController("/", "/libros");
        r.addViewController("/login").setViewName("login");
        r.addViewController("/access-denied").setViewName("access-denegado"); // si tu archivo se llama access-denied.html, usa ese nombre
      }
    };
  }

  @Bean
  CommandLineRunner seed(UsuarioRepository usuarios,
                         AutorRepository autores,
                         CategoriaRepository categorias,
                         LibroRepository libros,
                         EjemplarRepository ejemplares,
                         PasswordEncoder encoder) {
    return args -> {
      // admin/admin123
      if (!usuarios.existsByUsername("admin")) {
        usuarios.save(new Usuario("admin", encoder.encode("admin123"), Rol.ADMIN));
      }
      // datos mínimos
      if (categorias.count() == 0) {
        var cat = categorias.save(new Categoria()); cat.setNombre("General"); categorias.save(cat);
      }
      if (autores.count() == 0) {
        var a = new Autor(); a.setNombre("Autor Demo"); a.setPais("CR"); autores.save(a);
      }
      if (libros.count() == 0) {
        var cat = categorias.findAll().get(0);
        var a = autores.findAll().get(0);
        var l = new Libro("Libro Demo", 3, cat);
        l.getAutores().add(a);
        libros.save(l);
        // ejemplares
        ejemplares.save(new Ejemplar(l));
        ejemplares.save(new Ejemplar(l));
      }
    };
  }
}
