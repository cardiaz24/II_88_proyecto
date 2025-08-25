package com.example.proyecto.repository;

import com.example.proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameIgnoreCase(String username);
Optional<Usuario> findByUsername(String username);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByUsername(String username);
    
@Query("SELECT u FROM Usuario u WHERE u.multaPendiente = 0 AND " +
       "(SELECT COUNT(p) FROM Prestamo p WHERE p.usuario = u AND p.estado = 'ACTIVO') < 2")
List<Usuario> findUsuariosHabilitadosParaPrestamo();

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);

}
