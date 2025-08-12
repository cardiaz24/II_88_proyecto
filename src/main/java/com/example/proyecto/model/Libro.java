package com.example.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="libro",
       uniqueConstraints=@UniqueConstraint(name="uk_libro_titulo_categoria", columnNames={"titulo","categoria_id"}))
public class Libro {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @NotBlank @Size(max=200) @Column(nullable=false, length=200)
  private String titulo;

  @NotNull @Min(0) @Column(nullable=false)
  private Integer unidades;

  @ManyToMany(fetch=FetchType.LAZY)
  @JoinTable(name="libro_autor",
      joinColumns=@JoinColumn(name="libro_id", foreignKey=@ForeignKey(name="fk_libroautor_libro")),
      inverseJoinColumns=@JoinColumn(name="autor_id", foreignKey=@ForeignKey(name="fk_libroautor_autor")),
      uniqueConstraints=@UniqueConstraint(name="uk_libro_autor", columnNames={"libro_id","autor_id"}))
  private Set<Autor> autores = new LinkedHashSet<>();

  @ManyToOne(fetch=FetchType.LAZY, optional=false)
  @JoinColumn(name="categoria_id", nullable=false, foreignKey=@ForeignKey(name="fk_libro_categoria"))
  private Categoria categoria;

  public Libro(){}
  public Libro(String t,Integer u,Categoria c){this.titulo=t; this.unidades=u; this.categoria=c;}

  public Long getId(){return id;}
  public String getTitulo(){return titulo;}
  public void setTitulo(String t){this.titulo=t;}
  public Integer getUnidades(){return unidades;}
  public void setUnidades(Integer u){this.unidades=u;}
  public Set<Autor> getAutores(){return autores;}
  public void setAutores(Set<Autor> a){this.autores=a;}
  public Categoria getCategoria(){return categoria;}
  public void setCategoria(Categoria c){this.categoria=c;}

  @Override public boolean equals(Object o){
    if(this==o) return true; if(!(o instanceof Libro l)) return false;
    return Objects.equals(titulo,l.titulo) && Objects.equals(categoria,l.categoria);
  }
  @Override public int hashCode(){ return Objects.hash(titulo,categoria); }
}