package dev.temnikov.service;

import dev.temnikov.domain.AppUser;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AppUser}.
 */
public interface AppUserService {

    /**
     * Save a appUser.
     *
     * @param appUser the entity to save.
     * @return the persisted entity.
     */
    AppUser save(AppUser appUser);

    /**
     * Get all the appUsers.
     *
     * @return the list of entities.
     */
    List<AppUser> findAll();


    /**
     * Get the "id" appUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUser> findOne(Long id);

    /**
     * Delete the "id" appUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
