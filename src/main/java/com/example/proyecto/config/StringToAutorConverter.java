package com.example.proyecto.config;

import com.example.proyecto.model.Autor;
import com.example.proyecto.repository.AutorRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class StringToAutorConverter implements Converter<String, Autor> {
  private final AutorRepository repo;
  public StringToAutorConverter(AutorRepository repo) { this.repo = repo; }

  @Override
  @Nullable
  public Autor convert(@NonNull String source) {
    if (source.isBlank()) return null;
    try {
      return repo.findById(Long.parseLong(source)).orElse(null);
    } catch (NumberFormatException ex) {
      return null;
    }
  }
}
