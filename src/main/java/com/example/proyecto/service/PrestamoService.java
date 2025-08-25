package com.example.proyecto.service;

import com.example.proyecto.model.*;
import com.example.proyecto.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PrestamoService {
    private static final BigDecimal MULTA_DIARIA = BigDecimal.valueOf(300);
    private static final int DIAS_PRESTAMO = 14;
    private static final int MAX_PRESTAMOS_ACTIVOS = 2;

    private final PrestamoRepository prestamoRepo;
    private final EjemplarRepository ejemplarRepo;
    private final UsuarioRepository usuarioRepo;
    private final NotificacionService notificacionService;

    public PrestamoService(PrestamoRepository prestamoRepo, EjemplarRepository ejemplarRepo,
                         UsuarioRepository usuarioRepo, NotificacionService notificacionService) {
        this.prestamoRepo = prestamoRepo;
        this.ejemplarRepo = ejemplarRepo;
        this.usuarioRepo = usuarioRepo;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public Prestamo prestar(Usuario usuario, Ejemplar ejemplar) {
        // Validaciones
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

        // Actualizar ejemplar
        ejemplar.setPrestado(true);
        ejemplarRepo.save(ejemplar);

        // Guardar préstamo
        Prestamo prestamoGuardado = prestamoRepo.save(prestamo);
        
        // Notificar al usuario
        notificacionService.enviarNotificacionPrestamo(usuario, ejemplar, prestamoGuardado.getFechaVence());
        
        return prestamoGuardado;
    }

    private void validarPrestamo(Usuario usuario, Ejemplar ejemplar) {
        // 1. Verificar que el usuario no tenga multas pendientes
        BigDecimal multaPendiente = prestamoRepo.sumMultasPendientesByUsuarioId(usuario.getId());
        if (multaPendiente.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Usuario con multas pendientes: ₡" + multaPendiente);
        }

        // 2. Verificar que el ejemplar esté disponible
        if (ejemplar.isPrestado()) {
            throw new IllegalStateException("El ejemplar ya está prestado");
        }

        // 3. Verificar máximo de préstamos activos
        long prestamosActivos = prestamoRepo.countByUsuarioIdAndEstado(usuario.getId(), PrestamoEstado.ACTIVO);
        if (prestamosActivos >= MAX_PRESTAMOS_ACTIVOS) {
            throw new IllegalStateException("Máximo " + MAX_PRESTAMOS_ACTIVOS + " préstamos activos por usuario");
        }
    }

    @Transactional
    public Prestamo devolver(Long prestamoId) {
        Prestamo prestamo = prestamoRepo.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        if (prestamo.getEstado() != PrestamoEstado.ACTIVO) {
            return prestamo;
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        long diasAtraso = Math.max(0, ChronoUnit.DAYS.between(prestamo.getFechaVence(), prestamo.getFechaDevolucion()));

        if (diasAtraso > 0) {
            BigDecimal multa = MULTA_DIARIA.multiply(BigDecimal.valueOf(diasAtraso));
            prestamo.setMulta(multa);
            prestamo.setEstado(PrestamoEstado.CON_MORA);
            
            // Actualizar multa pendiente del usuario
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
            // Restar multa del usuario
            Usuario usuario = prestamo.getUsuario();
            usuario.setMultaPendiente(usuario.getMultaPendiente().subtract(prestamo.getMulta()));
            usuarioRepo.save(usuario);
            
            // Limpiar multa del préstamo
            prestamo.setMulta(BigDecimal.ZERO);
            
            // Cambiar estado si estaba en mora
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

        // Solo permitir eliminar préstamos activos
        if (prestamo.getEstado() == PrestamoEstado.ACTIVO) {
            // Liberar ejemplar
            Ejemplar ejemplar = prestamo.getEjemplar();
            ejemplar.setPrestado(false);
            ejemplarRepo.save(ejemplar);
        }
        
        prestamoRepo.delete(prestamo);
    }

    public List<Prestamo> obtenerPrestamosPorUsuario(Long usuarioId) {
        return prestamoRepo.findByUsuarioId(usuarioId);
    }

    public List<Prestamo> obtenerPrestamosVencidos() {
        return prestamoRepo.findByEstadoAndFechaVenceBefore(PrestamoEstado.ACTIVO, LocalDate.now());
    }

    public List<Prestamo> obtenerPrestamosActivos() {
        return prestamoRepo.findByEstado(PrestamoEstado.ACTIVO);
    }
}