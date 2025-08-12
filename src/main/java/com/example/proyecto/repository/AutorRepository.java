package com.example.proyecto.repository;
import com.example.proyecto.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface AutorRepository extends JpaRepository<Autor, Long> {
  Optional<Autor> findByNombreIgnoreCase(String nombre);
  Optional<Autor> findByNombreIgnoreCaseAndPaisIgnoreCase(String nombre, String pais);
  boolean existsByNombreIgnoreCaseAndPaisIgnoreCase(String nombre, String pais);
  List<Autor> findByPaisIgnoreCaseOrderByNombreAsc(String pais);
}