package dev.temnikov.web.rest;

import dev.temnikov.GarbageApp;
import dev.temnikov.domain.Shift;
import dev.temnikov.repository.ShiftRepository;
import dev.temnikov.service.ShiftService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ShiftResource} REST controller.
 */
@SpringBootTest(classes = GarbageApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ShiftResourceIT {

    private static final Instant DEFAULT_SHIFT_PLAN_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIFT_PLAN_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SHIFT_FACT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIFT_FACT_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SHIFT_PLAN_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIFT_PLAN_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SHIFT_FACT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIFT_FACT_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PREPAID = false;
    private static final Boolean UPDATED_PREPAID = true;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShiftMockMvc;

    private Shift shift;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shift createEntity(EntityManager em) {
        Shift shift = new Shift()
            .shiftPlanStartDate(DEFAULT_SHIFT_PLAN_START_DATE)
            .shiftFactStartDate(DEFAULT_SHIFT_FACT_START_DATE)
            .shiftPlanEndDate(DEFAULT_SHIFT_PLAN_END_DATE)
            .shiftFactEndDate(DEFAULT_SHIFT_FACT_END_DATE)
            .prepaid(DEFAULT_PREPAID);
        return shift;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shift createUpdatedEntity(EntityManager em) {
        Shift shift = new Shift()
            .shiftPlanStartDate(UPDATED_SHIFT_PLAN_START_DATE)
            .shiftFactStartDate(UPDATED_SHIFT_FACT_START_DATE)
            .shiftPlanEndDate(UPDATED_SHIFT_PLAN_END_DATE)
            .shiftFactEndDate(UPDATED_SHIFT_FACT_END_DATE)
            .prepaid(UPDATED_PREPAID);
        return shift;
    }

    @BeforeEach
    public void initTest() {
        shift = createEntity(em);
    }

    @Test
    @Transactional
    public void createShift() throws Exception {
        int databaseSizeBeforeCreate = shiftRepository.findAll().size();
        // Create the Shift
        restShiftMockMvc.perform(post("/api/shifts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shift)))
            .andExpect(status().isCreated());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeCreate + 1);
        Shift testShift = shiftList.get(shiftList.size() - 1);
        assertThat(testShift.getShiftPlanStartDate()).isEqualTo(DEFAULT_SHIFT_PLAN_START_DATE);
        assertThat(testShift.getShiftFactStartDate()).isEqualTo(DEFAULT_SHIFT_FACT_START_DATE);
        assertThat(testShift.getShiftPlanEndDate()).isEqualTo(DEFAULT_SHIFT_PLAN_END_DATE);
        assertThat(testShift.getShiftFactEndDate()).isEqualTo(DEFAULT_SHIFT_FACT_END_DATE);
        assertThat(testShift.isPrepaid()).isEqualTo(DEFAULT_PREPAID);
    }

    @Test
    @Transactional
    public void createShiftWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shiftRepository.findAll().size();

        // Create the Shift with an existing ID
        shift.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShiftMockMvc.perform(post("/api/shifts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shift)))
            .andExpect(status().isBadRequest());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllShifts() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        // Get all the shiftList
        restShiftMockMvc.perform(get("/api/shifts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shift.getId().intValue())))
            .andExpect(jsonPath("$.[*].shiftPlanStartDate").value(hasItem(DEFAULT_SHIFT_PLAN_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].shiftFactStartDate").value(hasItem(DEFAULT_SHIFT_FACT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].shiftPlanEndDate").value(hasItem(DEFAULT_SHIFT_PLAN_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].shiftFactEndDate").value(hasItem(DEFAULT_SHIFT_FACT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].prepaid").value(hasItem(DEFAULT_PREPAID.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getShift() throws Exception {
        // Initialize the database
        shiftRepository.saveAndFlush(shift);

        // Get the shift
        restShiftMockMvc.perform(get("/api/shifts/{id}", shift.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shift.getId().intValue()))
            .andExpect(jsonPath("$.shiftPlanStartDate").value(DEFAULT_SHIFT_PLAN_START_DATE.toString()))
            .andExpect(jsonPath("$.shiftFactStartDate").value(DEFAULT_SHIFT_FACT_START_DATE.toString()))
            .andExpect(jsonPath("$.shiftPlanEndDate").value(DEFAULT_SHIFT_PLAN_END_DATE.toString()))
            .andExpect(jsonPath("$.shiftFactEndDate").value(DEFAULT_SHIFT_FACT_END_DATE.toString()))
            .andExpect(jsonPath("$.prepaid").value(DEFAULT_PREPAID.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingShift() throws Exception {
        // Get the shift
        restShiftMockMvc.perform(get("/api/shifts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShift() throws Exception {
        // Initialize the database
        shiftService.save(shift);

        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();

        // Update the shift
        Shift updatedShift = shiftRepository.findById(shift.getId()).get();
        // Disconnect from session so that the updates on updatedShift are not directly saved in db
        em.detach(updatedShift);
        updatedShift
            .shiftPlanStartDate(UPDATED_SHIFT_PLAN_START_DATE)
            .shiftFactStartDate(UPDATED_SHIFT_FACT_START_DATE)
            .shiftPlanEndDate(UPDATED_SHIFT_PLAN_END_DATE)
            .shiftFactEndDate(UPDATED_SHIFT_FACT_END_DATE)
            .prepaid(UPDATED_PREPAID);

        restShiftMockMvc.perform(put("/api/shifts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedShift)))
            .andExpect(status().isOk());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
        Shift testShift = shiftList.get(shiftList.size() - 1);
        assertThat(testShift.getShiftPlanStartDate()).isEqualTo(UPDATED_SHIFT_PLAN_START_DATE);
        assertThat(testShift.getShiftFactStartDate()).isEqualTo(UPDATED_SHIFT_FACT_START_DATE);
        assertThat(testShift.getShiftPlanEndDate()).isEqualTo(UPDATED_SHIFT_PLAN_END_DATE);
        assertThat(testShift.getShiftFactEndDate()).isEqualTo(UPDATED_SHIFT_FACT_END_DATE);
        assertThat(testShift.isPrepaid()).isEqualTo(UPDATED_PREPAID);
    }

    @Test
    @Transactional
    public void updateNonExistingShift() throws Exception {
        int databaseSizeBeforeUpdate = shiftRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShiftMockMvc.perform(put("/api/shifts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shift)))
            .andExpect(status().isBadRequest());

        // Validate the Shift in the database
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShift() throws Exception {
        // Initialize the database
        shiftService.save(shift);

        int databaseSizeBeforeDelete = shiftRepository.findAll().size();

        // Delete the shift
        restShiftMockMvc.perform(delete("/api/shifts/{id}", shift.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shift> shiftList = shiftRepository.findAll();
        assertThat(shiftList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
