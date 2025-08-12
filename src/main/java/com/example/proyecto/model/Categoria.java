package com.example.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "nombre" }))
public class Categoria {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String nombre;

    @OneToMany(mappedBy = "categoria")
    private List<Libro> libros;
}
