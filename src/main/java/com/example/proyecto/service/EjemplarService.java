package com.example.proyecto.service;

import com.example.proyecto.model.Ejemplar;
import com.example.proyecto.repository.EjemplarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



@Service
@Transactional
public class EjemplarService {
    private final EjemplarRepository ejemplarRepository;

    public EjemplarService(EjemplarRepository ejemplarRepository) {
        this.ejemplarRepository = ejemplarRepository;
    }

    public List<Ejemplar> findByPrestadoFalse() {
        return ejemplarRepository.findByPrestadoFalse();
    }
}
