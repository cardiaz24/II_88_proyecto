package com.example.proyecto.service;

import com.example.proyecto.model.Libro;
import com.example.proyecto.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
  private final LibroRepository repo;
  public LibroService(LibroRepository repo){ this.repo=repo; }

  public List<Libro> findAll(){ return repo.findAll(); }
  public Optional<Libro> findById(Long id){ return repo.findById(id); }
  public Libro save(Libro l){ return repo.save(l); }
  public void deleteById(Long id){ repo.deleteById(id); }


public boolean existePorTituloYCategoria(String titulo, Long categoriaId) {
  return repo.existsByTituloIgnoreCaseAndCategoria_Id(titulo, categoriaId);
}

public boolean existePorTituloYCategoriaYIdNo(String titulo, Long categoriaId, Long id) {
  return repo.existsByTituloIgnoreCaseAndCategoria_IdAndIdNot(titulo, categoriaId, id);
}

}
