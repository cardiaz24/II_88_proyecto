package com.example.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Column(nullable = false)
    private String titulo;

    @Column(unique = true)
    private String isbn;

    private Integer anioPublicacion;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "Las unidades son obligatorias")
    @Column(nullable = false)
    private Integer unidades = 0;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
        name = "libro_autor",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private List<Ejemplar> ejemplares = new ArrayList<>();





    // Constructores, getters y setters...
    public Libro() {}

    public Libro(String titulo, Integer unidades, Categoria categoria) {
        this.titulo = titulo;
        this.unidades = unidades;
        this.categoria = categoria;
    }


   
}