package com.example.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "categoria", uniqueConstraints = @UniqueConstraint(name = "uk_categoria_nombre", columnNames = {
        "nombre" }))
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nombre;

    @OneToMany(mappedBy = "categoria")
    private List<Libro> libros;

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
