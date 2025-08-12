package com.example.proyecto.model;

import jakarta.persistence.*;

@Entity @Table(name="ejemplar")
public class Ejemplar {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY, optional=false)
  @JoinColumn(name="libro_id", nullable=false, foreignKey=@ForeignKey(name="fk_ejemplar_libro"))
  private Libro libro;

  @Column(nullable=false)
  private boolean prestado = false; // true si está prestado

  protected Ejemplar(){}
  public Ejemplar(Libro libro){ this.libro=libro; }

  public Long getId(){return id;}
  public Libro getLibro(){return libro;}
  public void setLibro(Libro libro){this.libro=libro;}
  public boolean isPrestado(){return prestado;}
  public void setPrestado(boolean p){this.prestado=p;}
}
