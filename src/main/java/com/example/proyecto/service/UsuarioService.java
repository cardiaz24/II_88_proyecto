package com.example.proyecto.service;

import com.example.proyecto.model.Usuario;

import com.example.proyecto.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

  private final UsuarioRepository repo;
  private final PasswordEncoder passwordEncoder;

  public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder) {
    this.repo = repo;
    this.passwordEncoder = encoder;
  }

  // Buscar usuario por username
  public Optional<Usuario> findByUsername(String username) {
    return repo.findByUsernameIgnoreCase(username);
  }

 public boolean existeUsername(String username) {
    return repo.existsByUsernameIgnoreCase(username);
  }

   public void crearUsuario(Usuario usuario) {
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    repo.save(usuario);
  }

  // Guardar usuario (nuevo registro)
  public Usuario registrar(Usuario usuario) {
    // Verifica si ya existe
    if (repo.existsByUsernameIgnoreCase(usuario.getUsername())) {
      throw new RuntimeException("El nombre de usuario ya está en uso");
    }

    // Encripta la contraseña
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

    // Habilita al usuario
    usuario.setEnabled(true);

    // Guarda en BD
    return repo.save(usuario);
  }

  public Optional<Usuario> findById(Long id) {
    return repo.findById(id);
  }

  public void save(Usuario usuario) {
    repo.save(usuario);
  }

 
  
}
