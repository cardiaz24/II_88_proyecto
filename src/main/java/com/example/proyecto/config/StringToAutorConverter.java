package com.example.proyecto.config;

import com.example.proyecto.model.Autor;
import com.example.proyecto.repository.AutorRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAutorConverter implements Converter<String, Autor> {
  private final AutorRepository repo;
  public StringToAutorConverter(AutorRepository repo) { this.repo = repo; }

  @Override public Autor convert(String source) {
    if (source == null || source.isBlank()) return null;
    return repo.findById(Long.valueOf(source)).orElse(null);
  }
}