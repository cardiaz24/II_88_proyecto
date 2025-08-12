package com.example.proyecto.repository;
import com.example.proyecto.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface LibroRepository extends JpaRepository<Libro, Long> {
  Optional<Libro> findByTituloIgnoreCase(String titulo);
  Optional<Libro> findByTituloIgnoreCaseAndCategoria_Id(String titulo, Long categoriaId);
  List<Libro> findByCategoria_IdOrderByTituloAsc(Long categoriaId);
  List<Libro> findByAutores_Id(Long autorId);
  boolean existsByTituloIgnoreCaseAndCategoria_Id(String titulo, Long categoriaId);
}