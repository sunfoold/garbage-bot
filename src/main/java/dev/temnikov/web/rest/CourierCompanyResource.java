package dev.temnikov.web.rest;

import dev.temnikov.domain.CourierCompany;
import dev.temnikov.service.CourierCompanyService;
import dev.temnikov.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link dev.temnikov.domain.CourierCompany}.
 */
@RestController
@RequestMapping("/api")
public class CourierCompanyResource {

    private final Logger log = LoggerFactory.getLogger(CourierCompanyResource.class);

    private static final String ENTITY_NAME = "courierCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourierCompanyService courierCompanyService;

    public CourierCompanyResource(CourierCompanyService courierCompanyService) {
        this.courierCompanyService = courierCompanyService;
    }

    /**
     * {@code POST  /courier-companies} : Create a new courierCompany.
     *
     * @param courierCompany the courierCompany to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courierCompany, or with status {@code 400 (Bad Request)} if the courierCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courier-companies")
    public ResponseEntity<CourierCompany> createCourierCompany(@RequestBody CourierCompany courierCompany) throws URISyntaxException {
        log.debug("REST request to save CourierCompany : {}", courierCompany);
        if (courierCompany.getId() != null) {
            throw new BadRequestAlertException("A new courierCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourierCompany result = courierCompanyService.save(courierCompany);
        return ResponseEntity.created(new URI("/api/courier-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courier-companies} : Updates an existing courierCompany.
     *
     * @param courierCompany the courierCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courierCompany,
     * or with status {@code 400 (Bad Request)} if the courierCompany is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courierCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courier-companies")
    public ResponseEntity<CourierCompany> updateCourierCompany(@RequestBody CourierCompany courierCompany) throws URISyntaxException {
        log.debug("REST request to update CourierCompany : {}", courierCompany);
        if (courierCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourierCompany result = courierCompanyService.save(courierCompany);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courierCompany.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /courier-companies} : get all the courierCompanies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courierCompanies in body.
     */
    @GetMapping("/courier-companies")
    public List<CourierCompany> getAllCourierCompanies() {
        log.debug("REST request to get all CourierCompanies");
        return courierCompanyService.findAll();
    }

    /**
     * {@code GET  /courier-companies/:id} : get the "id" courierCompany.
     *
     * @param id the id of the courierCompany to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courierCompany, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courier-companies/{id}")
    public ResponseEntity<CourierCompany> getCourierCompany(@PathVariable Long id) {
        log.debug("REST request to get CourierCompany : {}", id);
        Optional<CourierCompany> courierCompany = courierCompanyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courierCompany);
    }

    /**
     * {@code DELETE  /courier-companies/:id} : delete the "id" courierCompany.
     *
     * @param id the id of the courierCompany to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courier-companies/{id}")
    public ResponseEntity<Void> deleteCourierCompany(@PathVariable Long id) {
        log.debug("REST request to delete CourierCompany : {}", id);
        courierCompanyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
