package com.example.proyecto.repository;
import com.example.proyecto.model.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {
  List<Ejemplar> findByLibro_Id(Long libroId);
  long countByLibro_IdAndPrestadoFalse(Long libroId);
  List<Ejemplar> findByPrestadoFalse();

}
