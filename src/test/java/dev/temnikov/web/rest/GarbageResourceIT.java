package dev.temnikov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.temnikov.GarbageApp;
import dev.temnikov.domain.Garbage;
import dev.temnikov.repository.GarbageRepository;
import dev.temnikov.service.GarbageService;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GarbageResource} REST controller.
 */
@SpringBootTest(classes = GarbageApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class GarbageResourceIT {
    private static final Long DEFAULT_POCKETS = 1L;
    private static final Long UPDATED_POCKETS = 2L;

    private static final Long DEFAULT_HUGE_THINGS = 1L;
    private static final Long UPDATED_HUGE_THINGS = 2L;

    @Autowired
    private GarbageRepository garbageRepository;

    @Autowired
    private GarbageService garbageService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGarbageMockMvc;

    private Garbage garbage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Garbage createEntity(EntityManager em) {
        Garbage garbage = new Garbage().pockets(DEFAULT_POCKETS).hugeThings(DEFAULT_HUGE_THINGS);
        return garbage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Garbage createUpdatedEntity(EntityManager em) {
        Garbage garbage = new Garbage().pockets(UPDATED_POCKETS).hugeThings(UPDATED_HUGE_THINGS);
        return garbage;
    }

    @BeforeEach
    public void initTest() {
        garbage = createEntity(em);
    }

    @Test
    @Transactional
    public void createGarbage() throws Exception {
        int databaseSizeBeforeCreate = garbageRepository.findAll().size();
        // Create the Garbage
        restGarbageMockMvc
            .perform(post("/api/garbage").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(garbage)))
            .andExpect(status().isCreated());

        // Validate the Garbage in the database
        List<Garbage> garbageList = garbageRepository.findAll();
        assertThat(garbageList).hasSize(databaseSizeBeforeCreate + 1);
        Garbage testGarbage = garbageList.get(garbageList.size() - 1);
        assertThat(testGarbage.getPockets()).isEqualTo(DEFAULT_POCKETS);
        assertThat(testGarbage.getHugeThings()).isEqualTo(DEFAULT_HUGE_THINGS);
    }

    @Test
    @Transactional
    public void createGarbageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = garbageRepository.findAll().size();

        // Create the Garbage with an existing ID
        garbage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGarbageMockMvc
            .perform(post("/api/garbage").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(garbage)))
            .andExpect(status().isBadRequest());

        // Validate the Garbage in the database
        List<Garbage> garbageList = garbageRepository.findAll();
        assertThat(garbageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGarbage() throws Exception {
        // Initialize the database
        garbageRepository.saveAndFlush(garbage);

        // Get all the garbageList
        restGarbageMockMvc
            .perform(get("/api/garbage?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(garbage.getId().intValue())))
            .andExpect(jsonPath("$.[*].pockets").value(hasItem(DEFAULT_POCKETS.intValue())))
            .andExpect(jsonPath("$.[*].hugeThings").value(hasItem(DEFAULT_HUGE_THINGS.intValue())));
    }

    @Test
    @Transactional
    public void getGarbage() throws Exception {
        // Initialize the database
        garbageRepository.saveAndFlush(garbage);

        // Get the garbage
        restGarbageMockMvc
            .perform(get("/api/garbage/{id}", garbage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(garbage.getId().intValue()))
            .andExpect(jsonPath("$.pockets").value(DEFAULT_POCKETS.intValue()))
            .andExpect(jsonPath("$.hugeThings").value(DEFAULT_HUGE_THINGS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGarbage() throws Exception {
        // Get the garbage
        restGarbageMockMvc.perform(get("/api/garbage/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGarbage() throws Exception {
        // Initialize the database
        garbageService.save(garbage);

        int databaseSizeBeforeUpdate = garbageRepository.findAll().size();

        // Update the garbage
        Garbage updatedGarbage = garbageRepository.findById(garbage.getId()).get();
        // Disconnect from session so that the updates on updatedGarbage are not directly saved in db
        em.detach(updatedGarbage);
        updatedGarbage.pockets(UPDATED_POCKETS).hugeThings(UPDATED_HUGE_THINGS);

        restGarbageMockMvc
            .perform(put("/api/garbage").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedGarbage)))
            .andExpect(status().isOk());

        // Validate the Garbage in the database
        List<Garbage> garbageList = garbageRepository.findAll();
        assertThat(garbageList).hasSize(databaseSizeBeforeUpdate);
        Garbage testGarbage = garbageList.get(garbageList.size() - 1);
        assertThat(testGarbage.getPockets()).isEqualTo(UPDATED_POCKETS);
        assertThat(testGarbage.getHugeThings()).isEqualTo(UPDATED_HUGE_THINGS);
    }

    @Test
    @Transactional
    public void updateNonExistingGarbage() throws Exception {
        int databaseSizeBeforeUpdate = garbageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGarbageMockMvc
            .perform(put("/api/garbage").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(garbage)))
            .andExpect(status().isBadRequest());

        // Validate the Garbage in the database
        List<Garbage> garbageList = garbageRepository.findAll();
        assertThat(garbageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGarbage() throws Exception {
        // Initialize the database
        garbageService.save(garbage);

        int databaseSizeBeforeDelete = garbageRepository.findAll().size();

        // Delete the garbage
        restGarbageMockMvc
            .perform(delete("/api/garbage/{id}", garbage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Garbage> garbageList = garbageRepository.findAll();
        assertThat(garbageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
