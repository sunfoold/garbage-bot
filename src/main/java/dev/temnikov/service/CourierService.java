package dev.temnikov.service;

import dev.temnikov.domain.Courier;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Courier}.
 */
public interface CourierService {

    /**
     * Save a courier.
     *
     * @param courier the entity to save.
     * @return the persisted entity.
     */
    Courier save(Courier courier);

    /**
     * Get all the couriers.
     *
     * @return the list of entities.
     */
    List<Courier> findAll();


    /**
     * Get the "id" courier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Courier> findOne(Long id);

    /**
     * Delete the "id" courier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
