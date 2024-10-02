package org.e2e.e2e.Adopcion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdopcionService {

    private final AdopcionRepository adopcionRepository;

    public List<Adopcion> obtenerTodasLasAdopciones() {
        return adopcionRepository.findAll();
    }

    public Adopcion registrarAdopcion(Adopcion adopcion) {
        return adopcionRepository.save(adopcion);
    }

    public Adopcion obtenerAdopcionPorId(Long id) {
        return adopcionRepository.findById(id).orElseThrow(() -> new RuntimeException("Adopción no encontrada"));
    }

    public void eliminarAdopcion(Long id) {
        adopcionRepository.deleteById(id);
    }
}
