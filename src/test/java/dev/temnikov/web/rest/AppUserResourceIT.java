package dev.temnikov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.temnikov.GarbageApp;
import dev.temnikov.domain.AppUser;
import dev.temnikov.repository.AppUserRepository;
import dev.temnikov.service.AppUserService;
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
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@SpringBootTest(classes = GarbageApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AppUserResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_TELEGRAM_CHAT_ID = 1L;
    private static final Long UPDATED_TELEGRAM_CHAT_ID = 2L;

    private static final Long DEFAULT_BALANCE = 1L;
    private static final Long UPDATED_BALANCE = 2L;

    private static final String DEFAULT_PROMO_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROMO_CODE = "BBBBBBBBBB";

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .name(DEFAULT_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .telegramChatId(DEFAULT_TELEGRAM_CHAT_ID)
            .balance(DEFAULT_BALANCE)
            .promoCode(DEFAULT_PROMO_CODE);
        return appUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .telegramChatId(UPDATED_TELEGRAM_CHAT_ID)
            .balance(UPDATED_BALANCE)
            .promoCode(UPDATED_PROMO_CODE);
        return appUser;
    }

    @BeforeEach
    public void initTest() {
        appUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppUser() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();
        // Create the AppUser
        restAppUserMockMvc
            .perform(post("/api/app-users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate + 1);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAppUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getTelegramChatId()).isEqualTo(DEFAULT_TELEGRAM_CHAT_ID);
        assertThat(testAppUser.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testAppUser.getPromoCode()).isEqualTo(DEFAULT_PROMO_CODE);
    }

    @Test
    @Transactional
    public void createAppUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // Create the AppUser with an existing ID
        appUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc
            .perform(post("/api/app-users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppUsers() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc
            .perform(get("/api/app-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telegramChatId").value(hasItem(DEFAULT_TELEGRAM_CHAT_ID.intValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].promoCode").value(hasItem(DEFAULT_PROMO_CODE)));
    }

    @Test
    @Transactional
    public void getAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc
            .perform(get("/api/app-users/{id}", appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telegramChatId").value(DEFAULT_TELEGRAM_CHAT_ID.intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
            .andExpect(jsonPath("$.promoCode").value(DEFAULT_PROMO_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get("/api/app-users/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppUser() throws Exception {
        // Initialize the database
        appUserService.save(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).get();
        // Disconnect from session so that the updates on updatedAppUser are not directly saved in db
        em.detach(updatedAppUser);
        updatedAppUser
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .telegramChatId(UPDATED_TELEGRAM_CHAT_ID)
            .balance(UPDATED_BALANCE)
            .promoCode(UPDATED_PROMO_CODE);

        restAppUserMockMvc
            .perform(
                put("/api/app-users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAppUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getTelegramChatId()).isEqualTo(UPDATED_TELEGRAM_CHAT_ID);
        assertThat(testAppUser.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testAppUser.getPromoCode()).isEqualTo(UPDATED_PROMO_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(put("/api/app-users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppUser() throws Exception {
        // Initialize the database
        appUserService.save(appUser);

        int databaseSizeBeforeDelete = appUserRepository.findAll().size();

        // Delete the appUser
        restAppUserMockMvc
            .perform(delete("/api/app-users/{id}", appUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
