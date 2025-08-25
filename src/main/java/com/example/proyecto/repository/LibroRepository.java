package com.example.proyecto.repository;

import com.example.proyecto.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Métodos existentes
    boolean existsByTituloIgnoreCaseAndCategoria_Id(String titulo, Long categoriaId);
    boolean existsByTituloIgnoreCaseAndCategoria_IdAndIdNot(String titulo, Long categoriaId, Long id);
    
    // Nuevos métodos de búsqueda
    List<Libro> findByTituloContainingIgnoreCase(String titulo);
    List<Libro> findByCategoria_Id(Long categoriaId);
    List<Libro> findByAutores_Id(Long autorId);
    List<Libro> findByUnidadesGreaterThan(int unidades);
    List<Libro> findByUnidades(int unidades);
    



    // Métodos de contaje
    long countByUnidadesGreaterThan(int unidades);
    long countByUnidades(int unidades);
    
    // Método para verificar ejemplares prestados (debe ser implementado según tu modelo)
    @Query("SELECT COUNT(e) FROM Ejemplar e WHERE e.libro.id = :libroId AND e.prestado = true")
    long countEjemplaresPrestadosByLibroId(@Param("libroId") Long libroId);
    
    // Búsqueda avanzada
    @Query("SELECT l FROM Libro l JOIN l.autores a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :autor, '%'))")
    List<Libro> buscarPorNombreAutor(@Param("autor") String autor);
    
    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(l.isbn) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Libro> buscarPorTermino(@Param("termino") String termino);




}