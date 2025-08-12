package com.example.proyecto.service;

import com.example.proyecto.model.*;
import com.example.proyecto.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PrestamoService {
  private static final BigDecimal MULTA_DIARIA = BigDecimal.valueOf(300);

  private final PrestamoRepository prestamoRepo;
  private final EjemplarRepository ejemplarRepo;

  public PrestamoService(PrestamoRepository p, EjemplarRepository e){
    this.prestamoRepo = p;
    this.ejemplarRepo = e;
  }

  @Transactional
  public Prestamo prestar(Usuario usuario, Ejemplar ejemplar){
    // 1) No prestar si el usuario tiene multas pendientes
    boolean tieneMultas = prestamoRepo.existsByUsuario_IdAndEstadoAndMultaGreaterThan(
        usuario.getId(), PrestamoEstado.CON_MORA, BigDecimal.ZERO
    );
    if (tieneMultas) throw new IllegalStateException("Usuario con multas pendientes");

    // 2) No prestar si el ejemplar ya tiene un préstamo ACTIVO (validación de BD)
    boolean ejemplarOcupado = prestamoRepo.existsByEjemplar_IdAndEstado(ejemplar.getId(), PrestamoEstado.ACTIVO);
    if (ejemplarOcupado || ejemplar.isPrestado()) throw new IllegalStateException("El ejemplar ya está prestado");

    // 3) Máximo 2 préstamos activos por usuario
    long activos = prestamoRepo.countByUsuario_IdAndEstado(usuario.getId(), PrestamoEstado.ACTIVO);
    if (activos >= 2) throw new IllegalStateException("Máximo 2 préstamos activos por usuario");

    // Crear préstamo
    LocalDate hoy = LocalDate.now();
    Prestamo pr = new Prestamo(usuario, ejemplar, hoy);
    pr.setFechaVence(hoy.plusDays(14));
    pr.setMulta(BigDecimal.ZERO);
    pr.setEstado(PrestamoEstado.ACTIVO);

    ejemplar.setPrestado(true);
    ejemplarRepo.save(ejemplar);

    return prestamoRepo.save(pr);
  }

  @Transactional
  public Prestamo devolver(Prestamo pr){
    if (pr.getEstado() != PrestamoEstado.ACTIVO) return pr;

    pr.setFechaDevolucion(LocalDate.now());
    long diasAtraso = Math.max(0, ChronoUnit.DAYS.between(pr.getFechaVence(), pr.getFechaDevolucion()));

    if (diasAtraso > 0){
      pr.setMulta(MULTA_DIARIA.multiply(BigDecimal.valueOf(diasAtraso)));
      pr.setEstado(PrestamoEstado.CON_MORA);
    } else {
      pr.setEstado(PrestamoEstado.DEVUELTO);
    }

    Ejemplar ej = pr.getEjemplar();
    ej.setPrestado(false);
    ejemplarRepo.save(ej);

    return prestamoRepo.save(pr);
  }

  @Transactional
  public Prestamo limpiarMulta(Prestamo pr){
    pr.setMulta(BigDecimal.ZERO);
    if (pr.getEstado() == PrestamoEstado.CON_MORA){
      pr.setEstado(PrestamoEstado.DEVUELTO);
    }
    return prestamoRepo.save(pr);
  }
}
