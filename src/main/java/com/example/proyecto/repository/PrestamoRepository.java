package com.example.proyecto.repository;

import com.example.proyecto.model.Prestamo;
import com.example.proyecto.model.PrestamoEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    long countByUsuarioIdAndEstado(Long usuarioId, PrestamoEstado estado);
    
    boolean existsByEjemplarIdAndEstado(Long ejemplarId, PrestamoEstado estado);
    
    List<Prestamo> findByUsuarioId(Long usuarioId);
    
    List<Prestamo> findByUsuarioIdOrderByFechaPrestamoDesc(Long usuarioId);
    
    List<Prestamo> findByEstado(PrestamoEstado estado);
    
    List<Prestamo> findByEstadoAndFechaVenceBefore(PrestamoEstado estado, LocalDate fecha);
    
    @Query("SELECT COALESCE(SUM(p.multa), 0) FROM Prestamo p WHERE p.usuario.id = :usuarioId AND p.estado = 'CON_MORA'")
    BigDecimal sumMultasPendientesByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT p FROM Prestamo p WHERE p.usuario.id = :usuarioId AND p.estado = 'ACTIVO'")
    List<Prestamo> findPrestamosActivosByUsuarioId(@Param("usuarioId") Long usuarioId);
}