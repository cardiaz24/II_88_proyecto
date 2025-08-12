package com.example.proyecto.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nombre", "pais"}))
public class Autor {
    @Id @GeneratedValue
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String pais;

    @ManyToMany(mappedBy = "autores")
    private Set<Libro> libros;
}