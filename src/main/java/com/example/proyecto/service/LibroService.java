package com.example.proyecto.service;

import com.example.proyecto.model.Libro;
import com.example.proyecto.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibroService {
    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    // Métodos básicos CRUD
    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> obtenerPorId(Long id) {
        return libroRepository.findById(id);
    }

    @Transactional
    public Libro guardar(Libro libro) {
        validarLibro(libro);
        return libroRepository.save(libro);
    }

    @Transactional
    public void eliminarPorId(Long id) {
        // Verificar si el libro existe antes de eliminar
        if (!libroRepository.existsById(id)) {
            throw new IllegalArgumentException("El libro con ID " + id + " no existe");
        }
        
        // Verificar si el libro tiene ejemplares prestados
        if (tieneEjemplaresPrestados(id)) {
            throw new IllegalStateException("No se puede eliminar el libro porque tiene ejemplares prestados");
        }
        
        libroRepository.deleteById(id);
    }

    // Validaciones de existencia
    public boolean existePorId(Long id) {
        return libroRepository.existsById(id);
    }

    public boolean existePorTituloYCategoria(String titulo, Long categoriaId) {
        return libroRepository.existsByTituloIgnoreCaseAndCategoria_Id(titulo, categoriaId);
    }

    public boolean existePorTituloYCategoriaYIdNo(String titulo, Long categoriaId, Long id) {
        return libroRepository.existsByTituloIgnoreCaseAndCategoria_IdAndIdNot(titulo, categoriaId, id);
    }

    // Métodos de búsqueda
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Libro> buscarPorCategoria(Long categoriaId) {
        return libroRepository.findByCategoria_Id(categoriaId);
    }

    public List<Libro> buscarPorAutor(Long autorId) {
        return libroRepository.findByAutores_Id(autorId);
    }

    public List<Libro> buscarDisponibles() {
        return libroRepository.findByUnidadesGreaterThan(0);
    }

    public List<Libro> buscarAgotados() {
        return libroRepository.findByUnidades(0);
    }

    // Métodos de negocio
    public void reducirUnidades(Long libroId, int cantidad) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        
        if (libro.getUnidades() < cantidad) {
            throw new IllegalStateException("No hay suficientes unidades disponibles");
        }
        
        libro.setUnidades(libro.getUnidades() - cantidad);
        libroRepository.save(libro);
    }

    public void aumentarUnidades(Long libroId, int cantidad) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
        
        libro.setUnidades(libro.getUnidades() + cantidad);
        libroRepository.save(libro);
    }

    public boolean tieneUnidadesDisponibles(Long libroId) {
        return libroRepository.findById(libroId)
                .map(libro -> libro.getUnidades() > 0)
                .orElse(false);
    }

    public int obtenerUnidadesDisponibles(Long libroId) {
        return libroRepository.findById(libroId)
                .map(Libro::getUnidades)
                .orElse(0);
    }

    // Validaciones
    private void validarLibro(Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del libro es obligatorio");
        }
        
        if (libro.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría del libro es obligatoria");
        }
        
        if (libro.getUnidades() == null || libro.getUnidades() < 0) {
            throw new IllegalArgumentException("Las unidades deben ser un número positivo");
        }
        
        // Validar duplicados (solo si es un libro nuevo o está cambiando de categoría)
        if (libro.getId() == null) {
            if (existePorTituloYCategoria(libro.getTitulo(), libro.getCategoria().getId())) {
                throw new IllegalArgumentException("Ya existe un libro con el mismo título en esta categoría");
            }
        } else {
            if (existePorTituloYCategoriaYIdNo(libro.getTitulo(), libro.getCategoria().getId(), libro.getId())) {
                throw new IllegalArgumentException("Ya existe otro libro con el mismo título en esta categoría");
            }
        }
    }

    private boolean tieneEjemplaresPrestados(Long libroId) {
       
        return libroRepository.countEjemplaresPrestadosByLibroId(libroId) > 0;
    }

    // Métodos de reporting
    public long contarTotalLibros() {
        return libroRepository.count();
    }

    public long contarLibrosDisponibles() {
        return libroRepository.countByUnidadesGreaterThan(0);
    }

    public long contarLibrosAgotados() {
        return libroRepository.countByUnidades(0);
    }
}