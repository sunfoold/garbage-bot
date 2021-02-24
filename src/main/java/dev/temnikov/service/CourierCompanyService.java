package dev.temnikov.service;

import dev.temnikov.domain.CourierCompany;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CourierCompany}.
 */
public interface CourierCompanyService {

    /**
     * Save a courierCompany.
     *
     * @param courierCompany the entity to save.
     * @return the persisted entity.
     */
    CourierCompany save(CourierCompany courierCompany);

    /**
     * Get all the courierCompanies.
     *
     * @return the list of entities.
     */
    List<CourierCompany> findAll();


    /**
     * Get the "id" courierCompany.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourierCompany> findOne(Long id);

    /**
     * Delete the "id" courierCompany.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
