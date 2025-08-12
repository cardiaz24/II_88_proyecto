package com.example.proyecto.repository;
import com.example.proyecto.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
  Optional<Categoria> findByNombreIgnoreCase(String nombre);
  boolean existsByNombreIgnoreCase(String nombre);
  List<Categoria> findAllByOrderByNombreAsc();
}