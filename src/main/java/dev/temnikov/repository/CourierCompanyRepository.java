package dev.temnikov.repository;

import dev.temnikov.domain.CourierCompany;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CourierCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourierCompanyRepository extends JpaRepository<CourierCompany, Long> {
}
