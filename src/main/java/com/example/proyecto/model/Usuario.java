package com.example.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_username", columnNames = "username"))
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false, length = 80)
  private String username;

  @NotBlank
  @Column(nullable = false, length = 200)
  private String password; // BCrypt

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Rol rol;

  @Column(nullable = false)
  private boolean enabled = true;

  public Usuario() {
  }

  public Usuario(String username, String password, Rol rol) {
    this.username = username;
    this.password = password;
    this.rol = rol;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
