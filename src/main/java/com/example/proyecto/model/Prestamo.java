package com.example.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="prestamo",
  uniqueConstraints=@UniqueConstraint(name="uk_prestamo_ejemplar_activo", columnNames={"ejemplar_id","estado"}))
public class Prestamo {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY, optional=false)
  @JoinColumn(name="usuario_id", nullable=false, foreignKey=@ForeignKey(name="fk_prestamo_usuario"))
  private Usuario usuario;

  @ManyToOne(fetch=FetchType.LAZY, optional=false)
  @JoinColumn(name="ejemplar_id", nullable=false, foreignKey=@ForeignKey(name="fk_prestamo_ejemplar"))
  private Ejemplar ejemplar;

  @NotNull @Column(nullable=false)
  private LocalDate fechaPrestamo;

  private LocalDate fechaDevolucion; // fecha real de devolución

  @Column(nullable=false)
  private LocalDate fechaVence; // = fechaPrestamo + 14 días

  @Enumerated(EnumType.STRING)
  @Column(nullable=false, length=12)
  private PrestamoEstado estado = PrestamoEstado.ACTIVO;

  @Column(precision=10, scale=2)
  private BigDecimal multa = BigDecimal.ZERO;

  public Prestamo(){}
  public Prestamo(Usuario u, Ejemplar e, LocalDate f){ this.usuario=u; this.ejemplar=e; this.fechaPrestamo=f; }

  public Long getId(){return id;}
  public Usuario getUsuario(){return usuario;}
  public Ejemplar getEjemplar(){return ejemplar;}
  public LocalDate getFechaPrestamo(){return fechaPrestamo;}
  public LocalDate getFechaDevolucion(){return fechaDevolucion;}
  public void setFechaDevolucion(LocalDate f){this.fechaDevolucion=f;}
  public LocalDate getFechaVence(){return fechaVence;}
  public void setFechaVence(LocalDate f){this.fechaVence=f;}
  public PrestamoEstado getEstado(){return estado;}
  public void setEstado(PrestamoEstado e){this.estado=e;}
  public BigDecimal getMulta(){return multa;}
  public void setMulta(BigDecimal m){this.multa=m;}
}