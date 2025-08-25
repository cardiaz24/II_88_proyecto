package com.example.proyecto.service;

import com.example.proyecto.model.*;
import com.example.proyecto.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrestamoService {

    private static final BigDecimal MULTA_DIARIA = BigDecimal.valueOf(300);
    private static final int DIAS_PRESTAMO = 14;
    private static final int MAX_PRESTAMOS_ACTIVOS = 2;

    private final PrestamoRepository prestamoRepo;
    private final EjemplarRepository ejemplarRepo;
    private final UsuarioRepository usuarioRepo;

    /* --- CRUD/consultas usadas por el controlador --- */
    public List<Prestamo> findAll() {
        return prestamoRepo.findAll();
    }

    public Optional<Prestamo> findById(Long id) {
        return prestamoRepo.findById(id);
    }

    public List<Prestamo> findByUsuario(Usuario usuario) {
        return prestamoRepo.findByUsuario(usuario);
    }

    public List<Prestamo> obtenerPrestamosPorUsuario(Long usuarioId) {
        return prestamoRepo.findByUsuarioId(usuarioId);
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        return prestamoRepo.findByEstado(PrestamoEstado.ACTIVO);
    }

    public List<Prestamo> obtenerPrestamosVencidos() {
        return prestamoRepo.findByEstadoAndFechaVenceBefore(PrestamoEstado.ACTIVO, LocalDate.now());
    }

    /* --- Negocio --- */

    @Transactional
    public Prestamo prestar(Usuario usuario, Ejemplar ejemplar) {
        validarPrestamo(usuario, ejemplar);

        // Crear préstamo
        LocalDate hoy = LocalDate.now();
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaPrestamo(hoy);
        prestamo.setFechaVence(hoy.plusDays(DIAS_PRESTAMO));
        prestamo.setMulta(BigDecimal.ZERO);
        prestamo.setEstado(PrestamoEstado.ACTIVO);

        // Marcar ejemplar como prestado
        ejemplar.setPrestado(true);
        ejemplarRepo.save(ejemplar);

        return prestamoRepo.save(prestamo);
    }

    @Transactional
    public Prestamo devolver(Long prestamoId) {
        Prestamo prestamo = prestamoRepo.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        if (prestamo.getEstado() != PrestamoEstado.ACTIVO) {
            return prestamo; // nada que hacer
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        long diasAtraso = Math.max(0,
                ChronoUnit.DAYS.between(prestamo.getFechaVence(), prestamo.getFechaDevolucion()));

        if (diasAtraso > 0) {
            BigDecimal multa = MULTA_DIARIA.multiply(BigDecimal.valueOf(diasAtraso));
            prestamo.setMulta(multa);
            prestamo.setEstado(PrestamoEstado.CON_MORA);

            Usuario usuario = prestamo.getUsuario();
            usuario.setMultaPendiente(usuario.getMultaPendiente().add(multa));
            usuarioRepo.save(usuario);
        } else {
            prestamo.setEstado(PrestamoEstado.DEVUELTO);
        }

        // Liberar ejemplar
        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setPrestado(false);
        ejemplarRepo.save(ejemplar);

        return prestamoRepo.save(prestamo);
    }

    @Transactional
    public void limpiarMulta(Long prestamoId) {
        Prestamo prestamo = prestamoRepo.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        if (prestamo.getMulta().compareTo(BigDecimal.ZERO) > 0) {
            Usuario usuario = prestamo.getUsuario();
            usuario.setMultaPendiente(usuario.getMultaPendiente().subtract(prestamo.getMulta()));
            usuarioRepo.save(usuario);

            prestamo.setMulta(BigDecimal.ZERO);
            if (prestamo.getEstado() == PrestamoEstado.CON_MORA) {
                prestamo.setEstado(PrestamoEstado.DEVUELTO);
            }
            prestamoRepo.save(prestamo);
        }
    }

    @Transactional
    public void eliminarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepo.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        if (prestamo.getEstado() == PrestamoEstado.ACTIVO) {
            Ejemplar ejemplar = prestamo.getEjemplar();
            ejemplar.setPrestado(false);
            ejemplarRepo.save(ejemplar);
        }
        prestamoRepo.delete(prestamo);
    }

    /* --- Validaciones internas --- */
    private void validarPrestamo(Usuario usuario, Ejemplar ejemplar) {
        // Multas pendientes (puede venir null si no hay registros)
        BigDecimal multaPendiente = prestamoRepo.sumMultasPendientesByUsuarioId(usuario.getId());
        if (multaPendiente != null && multaPendiente.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Usuario con multas pendientes: ₡" + multaPendiente);
        }

        // Disponibilidad de ejemplar
        if (ejemplar.isPrestado()) {
            throw new IllegalStateException("El ejemplar ya está prestado");
        }

        // Máximo de préstamos activos
        long activos = prestamoRepo.countByUsuarioIdAndEstado(usuario.getId(), PrestamoEstado.ACTIVO);
        if (activos >= MAX_PRESTAMOS_ACTIVOS) {
            throw new IllegalStateException("Máximo " + MAX_PRESTAMOS_ACTIVOS + " préstamos activos por usuario");
        }
    }
}
