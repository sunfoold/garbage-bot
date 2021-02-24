package dev.temnikov.web.rest;

import dev.temnikov.domain.Garbage;
import dev.temnikov.service.GarbageService;
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
 * REST controller for managing {@link dev.temnikov.domain.Garbage}.
 */
@RestController
@RequestMapping("/api")
public class GarbageResource {

    private final Logger log = LoggerFactory.getLogger(GarbageResource.class);

    private static final String ENTITY_NAME = "garbage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GarbageService garbageService;

    public GarbageResource(GarbageService garbageService) {
        this.garbageService = garbageService;
    }

    /**
     * {@code POST  /garbage} : Create a new garbage.
     *
     * @param garbage the garbage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new garbage, or with status {@code 400 (Bad Request)} if the garbage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/garbage")
    public ResponseEntity<Garbage> createGarbage(@RequestBody Garbage garbage) throws URISyntaxException {
        log.debug("REST request to save Garbage : {}", garbage);
        if (garbage.getId() != null) {
            throw new BadRequestAlertException("A new garbage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Garbage result = garbageService.save(garbage);
        return ResponseEntity.created(new URI("/api/garbage/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /garbage} : Updates an existing garbage.
     *
     * @param garbage the garbage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated garbage,
     * or with status {@code 400 (Bad Request)} if the garbage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the garbage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/garbage")
    public ResponseEntity<Garbage> updateGarbage(@RequestBody Garbage garbage) throws URISyntaxException {
        log.debug("REST request to update Garbage : {}", garbage);
        if (garbage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Garbage result = garbageService.save(garbage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, garbage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /garbage} : get all the garbage.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of garbage in body.
     */
    @GetMapping("/garbage")
    public List<Garbage> getAllGarbage() {
        log.debug("REST request to get all Garbage");
        return garbageService.findAll();
    }

    /**
     * {@code GET  /garbage/:id} : get the "id" garbage.
     *
     * @param id the id of the garbage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the garbage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/garbage/{id}")
    public ResponseEntity<Garbage> getGarbage(@PathVariable Long id) {
        log.debug("REST request to get Garbage : {}", id);
        Optional<Garbage> garbage = garbageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(garbage);
    }

    /**
     * {@code DELETE  /garbage/:id} : delete the "id" garbage.
     *
     * @param id the id of the garbage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/garbage/{id}")
    public ResponseEntity<Void> deleteGarbage(@PathVariable Long id) {
        log.debug("REST request to delete Garbage : {}", id);
        garbageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
