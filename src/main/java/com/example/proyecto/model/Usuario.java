package com.example.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El nombre es obligatorio")
  @Column(nullable = false, length = 50)
  private String nombre;

  @NotBlank(message = "Los apellidos son obligatorios")
  @Column(nullable = false, length = 50)
  private String apellidos;

  @NotBlank(message = "El nombre de usuario es obligatorio")
  @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
  @Column(nullable = false, length = 20, unique = true)
  private String username;

  @NotBlank(message = "La contraseña es obligatoria")
  @Column(nullable = false)
  private String password;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  @Column(nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Rol rol;

  @Column(nullable = false)
  private boolean enabled = true;

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
  private List<Prestamo> prestamos = new ArrayList<>();

  @Column(columnDefinition = "double default 0.0")
  private double multaPendiente;

  // Constructores
  public Usuario() {
  }

  public Usuario(String nombre, String apellidos, String username, String password, String email, Rol rol) {
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.username = username;
    this.password = password;
    this.email = email;
    this.rol = rol;
  }

  // Getters y setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public List<Prestamo> getPrestamos() {
    return prestamos;
  }

  public void setPrestamos(List<Prestamo> prestamos) {
    this.prestamos = prestamos;
  }

  public double getMultaPendiente() {
    return multaPendiente;
  }

  public void setMultaPendiente(double multaPendiente) {
    this.multaPendiente = multaPendiente;
  }

  public String getNombreCompleto() {
    return nombre + " " + apellidos;
  }
}