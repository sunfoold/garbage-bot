package dev.temnikov.repository;

import dev.temnikov.domain.Garbage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Garbage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GarbageRepository extends JpaRepository<Garbage, Long> {
}
