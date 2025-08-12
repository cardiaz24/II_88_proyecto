package com.example.proyecto.config;

import com.example.proyecto.model.Categoria;
import com.example.proyecto.repository.CategoriaRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCategoriaConverter implements Converter<String, Categoria> {
  private final CategoriaRepository repo;
  public StringToCategoriaConverter(CategoriaRepository repo) { this.repo = repo; }

  @Override
  public Categoria convert(String source) {
    if (source == null || source.isBlank()) return null;
    return repo.findById(Long.valueOf(source)).orElse(null);
  }
}
