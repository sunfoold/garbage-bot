package dev.temnikov.service;

import dev.temnikov.domain.Garbage;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Garbage}.
 */
public interface GarbageService {

    /**
     * Save a garbage.
     *
     * @param garbage the entity to save.
     * @return the persisted entity.
     */
    Garbage save(Garbage garbage);

    /**
     * Get all the garbage.
     *
     * @return the list of entities.
     */
    List<Garbage> findAll();


    /**
     * Get the "id" garbage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Garbage> findOne(Long id);

    /**
     * Delete the "id" garbage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
