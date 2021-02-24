package dev.temnikov.web.rest;

import dev.temnikov.domain.Courier;
import dev.temnikov.service.CourierService;
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
 * REST controller for managing {@link dev.temnikov.domain.Courier}.
 */
@RestController
@RequestMapping("/api")
public class CourierResource {

    private final Logger log = LoggerFactory.getLogger(CourierResource.class);

    private static final String ENTITY_NAME = "courier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourierService courierService;

    public CourierResource(CourierService courierService) {
        this.courierService = courierService;
    }

    /**
     * {@code POST  /couriers} : Create a new courier.
     *
     * @param courier the courier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courier, or with status {@code 400 (Bad Request)} if the courier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/couriers")
    public ResponseEntity<Courier> createCourier(@RequestBody Courier courier) throws URISyntaxException {
        log.debug("REST request to save Courier : {}", courier);
        if (courier.getId() != null) {
            throw new BadRequestAlertException("A new courier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Courier result = courierService.save(courier);
        return ResponseEntity.created(new URI("/api/couriers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /couriers} : Updates an existing courier.
     *
     * @param courier the courier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courier,
     * or with status {@code 400 (Bad Request)} if the courier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/couriers")
    public ResponseEntity<Courier> updateCourier(@RequestBody Courier courier) throws URISyntaxException {
        log.debug("REST request to update Courier : {}", courier);
        if (courier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Courier result = courierService.save(courier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courier.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /couriers} : get all the couriers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of couriers in body.
     */
    @GetMapping("/couriers")
    public List<Courier> getAllCouriers() {
        log.debug("REST request to get all Couriers");
        return courierService.findAll();
    }

    /**
     * {@code GET  /couriers/:id} : get the "id" courier.
     *
     * @param id the id of the courier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/couriers/{id}")
    public ResponseEntity<Courier> getCourier(@PathVariable Long id) {
        log.debug("REST request to get Courier : {}", id);
        Optional<Courier> courier = courierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courier);
    }

    /**
     * {@code DELETE  /couriers/:id} : delete the "id" courier.
     *
     * @param id the id of the courier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/couriers/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable Long id) {
        log.debug("REST request to delete Courier : {}", id);
        courierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
