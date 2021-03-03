package dev.temnikov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.temnikov.GarbageApp;
import dev.temnikov.domain.CourierCompany;
import dev.temnikov.repository.CourierCompanyRepository;
import dev.temnikov.service.CourierCompanyService;
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
 * Integration tests for the {@link CourierCompanyResource} REST controller.
 */
@SpringBootTest(classes = GarbageApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CourierCompanyResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INN = "AAAAAAAAAA";
    private static final String UPDATED_INN = "BBBBBBBBBB";

    private static final String DEFAULT_OGRN = "AAAAAAAAAA";
    private static final String UPDATED_OGRN = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private CourierCompanyRepository courierCompanyRepository;

    @Autowired
    private CourierCompanyService courierCompanyService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourierCompanyMockMvc;

    private CourierCompany courierCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourierCompany createEntity(EntityManager em) {
        CourierCompany courierCompany = new CourierCompany()
            .name(DEFAULT_NAME)
            .inn(DEFAULT_INN)
            .ogrn(DEFAULT_OGRN)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .isActive(DEFAULT_IS_ACTIVE);
        return courierCompany;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourierCompany createUpdatedEntity(EntityManager em) {
        CourierCompany courierCompany = new CourierCompany()
            .name(UPDATED_NAME)
            .inn(UPDATED_INN)
            .ogrn(UPDATED_OGRN)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .isActive(UPDATED_IS_ACTIVE);
        return courierCompany;
    }

    @BeforeEach
    public void initTest() {
        courierCompany = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourierCompany() throws Exception {
        int databaseSizeBeforeCreate = courierCompanyRepository.findAll().size();
        // Create the CourierCompany
        restCourierCompanyMockMvc
            .perform(
                post("/api/courier-companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isCreated());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        CourierCompany testCourierCompany = courierCompanyList.get(courierCompanyList.size() - 1);
        assertThat(testCourierCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourierCompany.getInn()).isEqualTo(DEFAULT_INN);
        assertThat(testCourierCompany.getOgrn()).isEqualTo(DEFAULT_OGRN);
        assertThat(testCourierCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCourierCompany.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCourierCompany.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createCourierCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courierCompanyRepository.findAll().size();

        // Create the CourierCompany with an existing ID
        courierCompany.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourierCompanyMockMvc
            .perform(
                post("/api/courier-companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourierCompanies() throws Exception {
        // Initialize the database
        courierCompanyRepository.saveAndFlush(courierCompany);

        // Get all the courierCompanyList
        restCourierCompanyMockMvc
            .perform(get("/api/courier-companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courierCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].inn").value(hasItem(DEFAULT_INN)))
            .andExpect(jsonPath("$.[*].ogrn").value(hasItem(DEFAULT_OGRN)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCourierCompany() throws Exception {
        // Initialize the database
        courierCompanyRepository.saveAndFlush(courierCompany);

        // Get the courierCompany
        restCourierCompanyMockMvc
            .perform(get("/api/courier-companies/{id}", courierCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courierCompany.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.inn").value(DEFAULT_INN))
            .andExpect(jsonPath("$.ogrn").value(DEFAULT_OGRN))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCourierCompany() throws Exception {
        // Get the courierCompany
        restCourierCompanyMockMvc.perform(get("/api/courier-companies/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourierCompany() throws Exception {
        // Initialize the database
        courierCompanyService.save(courierCompany);

        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();

        // Update the courierCompany
        CourierCompany updatedCourierCompany = courierCompanyRepository.findById(courierCompany.getId()).get();
        // Disconnect from session so that the updates on updatedCourierCompany are not directly saved in db
        em.detach(updatedCourierCompany);
        updatedCourierCompany
            .name(UPDATED_NAME)
            .inn(UPDATED_INN)
            .ogrn(UPDATED_OGRN)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .isActive(UPDATED_IS_ACTIVE);

        restCourierCompanyMockMvc
            .perform(
                put("/api/courier-companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourierCompany))
            )
            .andExpect(status().isOk());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
        CourierCompany testCourierCompany = courierCompanyList.get(courierCompanyList.size() - 1);
        assertThat(testCourierCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourierCompany.getInn()).isEqualTo(UPDATED_INN);
        assertThat(testCourierCompany.getOgrn()).isEqualTo(UPDATED_OGRN);
        assertThat(testCourierCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCourierCompany.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCourierCompany.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingCourierCompany() throws Exception {
        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourierCompanyMockMvc
            .perform(
                put("/api/courier-companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourierCompany() throws Exception {
        // Initialize the database
        courierCompanyService.save(courierCompany);

        int databaseSizeBeforeDelete = courierCompanyRepository.findAll().size();

        // Delete the courierCompany
        restCourierCompanyMockMvc
            .perform(delete("/api/courier-companies/{id}", courierCompany.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
