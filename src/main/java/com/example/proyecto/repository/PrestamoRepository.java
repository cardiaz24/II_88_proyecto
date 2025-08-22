package com.example.proyecto.repository;

import com.example.proyecto.model.Prestamo;
import com.example.proyecto.model.PrestamoEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
  long countByUsuario_IdAndEstado(Long usuarioId, PrestamoEstado estado);

  boolean existsByEjemplar_IdAndEstado(Long ejemplarId, PrestamoEstado estado);

  boolean existsByUsuario_IdAndEstadoAndMultaGreaterThan(Long usuarioId, PrestamoEstado estado, BigDecimal monto);

  List<Prestamo> findByUsuario_IdAndEstado(Long usuarioId, PrestamoEstado estado);
  List<Prestamo> findByUsuario_IdOrderByFechaPrestamoDesc(Long id);
  List<Prestamo> findByUsuario_Id(Long id);
}