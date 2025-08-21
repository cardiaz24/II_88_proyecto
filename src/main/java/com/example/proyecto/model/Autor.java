package com.example.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "autor", uniqueConstraints = @UniqueConstraint(name = "uk_autor_nombre_pais", columnNames = { "nombre",
        "pais" }))
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nombre;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String pais;

    @ManyToMany(mappedBy = "autores")
    private Set<Libro> libros = new LinkedHashSet<>();

    public Autor() {
    }

    public Autor(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }

    // Getters y setters (necesarios para Thymeleaf/JPA)
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }

    // Opcional: helpers para la relación bidireccional
    public void addLibro(Libro l) {
        this.libros.add(l);
        l.getAutores().add(this);
    }

    public void removeLibro(Libro l) {
        this.libros.remove(l);
        l.getAutores().remove(this);
    }
}
