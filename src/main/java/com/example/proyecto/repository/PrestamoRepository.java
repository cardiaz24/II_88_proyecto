package com.example.proyecto.repository;
import com.example.proyecto.model.Prestamo;
import com.example.proyecto.model.PrestamoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
  long countByUsuario_IdAndEstado(Long usuarioId, PrestamoEstado estado);
  boolean existsByEjemplar_IdAndEstado(Long ejemplarId, PrestamoEstado estado);
  List<Prestamo> findByUsuario_IdAndEstado(Long usuarioId, PrestamoEstado estado);
}
