package com.example.proyecto.model;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ejemplar")
public class Ejemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @OneToOne(mappedBy = "ejemplar")
    private Prestamo prestamo;

    private boolean prestado = false;
    
    @Version
    private Long version; 
    // Constructores, getters y setters...
    public Ejemplar() {}

    public Ejemplar(String codigo, Libro libro) {
        this.codigo = codigo;
        this.libro = libro;
    }
}