package dev.temnikov.service;

import dev.temnikov.domain.Shift;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Shift}.
 */
public interface ShiftService {

    /**
     * Save a shift.
     *
     * @param shift the entity to save.
     * @return the persisted entity.
     */
    Shift save(Shift shift);

    /**
     * Get all the shifts.
     *
     * @return the list of entities.
     */
    List<Shift> findAll();


    /**
     * Get the "id" shift.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Shift> findOne(Long id);

    /**
     * Delete the "id" shift.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
