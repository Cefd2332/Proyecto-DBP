package org.e2e.e2e.Adoptante;

import org.e2e.e2e.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptanteService {

    private final AdoptanteRepository adoptanteRepository;

    @Autowired
    public AdoptanteService(AdoptanteRepository adoptanteRepository) {
        this.adoptanteRepository = adoptanteRepository;
    }

    /**
     * Crear un nuevo Adoptante.
     */
    public Adoptante crearAdoptante(AdoptanteRequestDto adoptanteRequestDto) {
        Adoptante adoptante = new Adoptante(
                adoptanteRequestDto.getNombre(),
                adoptanteRequestDto.getEmail(),
                adoptanteRequestDto.getDireccion(),
                adoptanteRequestDto.getTelefono()
        );
        // El deviceToken se genera automáticamente en @PrePersist
        return adoptanteRepository.save(adoptante);
    }

    /**
     * Obtener todos los Adoptantes.
     */
    public List<Adoptante> obtenerTodosLosAdoptantes() {
        return adoptanteRepository.findAll();
    }

    /**
     * Obtener un Adoptante por ID.
     */
    public Adoptante obtenerAdoptantePorId(Long id) {
        return adoptanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Adoptante no encontrado con ID: " + id));
    }

    /**
     * Actualizar un Adoptante existente.
     */
    public Adoptante actualizarAdoptante(Long id, AdoptanteRequestDto adoptanteRequestDto) {
        Adoptante adoptanteExistente = obtenerAdoptantePorId(id);
        adoptanteExistente.setNombre(adoptanteRequestDto.getNombre());
        adoptanteExistente.setEmail(adoptanteRequestDto.getEmail());
        adoptanteExistente.setDireccion(adoptanteRequestDto.getDireccion());
        adoptanteExistente.setTelefono(adoptanteRequestDto.getTelefono());
        // El deviceToken no se actualiza para mantener su unicidad
        return adoptanteRepository.save(adoptanteExistente);
    }

    /**
     * Eliminar un Adoptante.
     */
    public void eliminarAdoptante(Long id) {
        Adoptante adoptante = obtenerAdoptantePorId(id);
        adoptanteRepository.delete(adoptante);
    }
}
