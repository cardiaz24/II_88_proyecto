package com.example.proyecto.service;

import com.example.proyecto.model.Autor;
import com.example.proyecto.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AutorService {
    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> findAll() {
        return autorRepository.findAll();
    }

    public Optional<Autor> findById(Long id) {
        return autorRepository.findById(id);
    }

    public Autor save(Autor autor) {
        return autorRepository.save(autor);
    }

    public void deleteById(Long id) {
        autorRepository.deleteById(id);
    }

    public boolean existePorNombreYPais(String nombre, String pais) {
        return autorRepository.existsByNombreIgnoreCaseAndPaisIgnoreCase(nombre, pais);
    }

}