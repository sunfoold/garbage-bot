package dev.temnikov.web.rest;

import dev.temnikov.domain.Shift;
import dev.temnikov.service.ShiftService;
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
 * REST controller for managing {@link dev.temnikov.domain.Shift}.
 */
@RestController
@RequestMapping("/api")
public class ShiftResource {

    private final Logger log = LoggerFactory.getLogger(ShiftResource.class);

    private static final String ENTITY_NAME = "shift";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShiftService shiftService;

    public ShiftResource(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    /**
     * {@code POST  /shifts} : Create a new shift.
     *
     * @param shift the shift to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shift, or with status {@code 400 (Bad Request)} if the shift has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shifts")
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) throws URISyntaxException {
        log.debug("REST request to save Shift : {}", shift);
        if (shift.getId() != null) {
            throw new BadRequestAlertException("A new shift cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Shift result = shiftService.save(shift);
        return ResponseEntity.created(new URI("/api/shifts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shifts} : Updates an existing shift.
     *
     * @param shift the shift to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shift,
     * or with status {@code 400 (Bad Request)} if the shift is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shift couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shifts")
    public ResponseEntity<Shift> updateShift(@RequestBody Shift shift) throws URISyntaxException {
        log.debug("REST request to update Shift : {}", shift);
        if (shift.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Shift result = shiftService.save(shift);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shift.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shifts} : get all the shifts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shifts in body.
     */
    @GetMapping("/shifts")
    public List<Shift> getAllShifts() {
        log.debug("REST request to get all Shifts");
        return shiftService.findAll();
    }

    /**
     * {@code GET  /shifts/:id} : get the "id" shift.
     *
     * @param id the id of the shift to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shift, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shifts/{id}")
    public ResponseEntity<Shift> getShift(@PathVariable Long id) {
        log.debug("REST request to get Shift : {}", id);
        Optional<Shift> shift = shiftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shift);
    }

    /**
     * {@code DELETE  /shifts/:id} : delete the "id" shift.
     *
     * @param id the id of the shift to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shifts/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        log.debug("REST request to delete Shift : {}", id);
        shiftService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
