package dev.temnikov.web.rest;

import dev.temnikov.GarbageApp;
import dev.temnikov.domain.Order;
import dev.temnikov.repository.OrderRepository;
import dev.temnikov.service.OrderService;

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

import dev.temnikov.domain.enumeration.OrderStatus;
/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@SpringBootTest(classes = GarbageApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrderResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final Instant DEFAULT_FINISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USER_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_USER_PHOTO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COURIER_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_COURIER_PHOTO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_END_ORDER_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_END_ORDER_PHOTO_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_ORDER_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ORDER_FINISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_FINISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.NEW;
    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.ASSIGNED;

    private static final Long DEFAULT_COURIER_RATIO = 1L;
    private static final Long UPDATED_COURIER_RATIO = 2L;

    private static final Long DEFAULT_USER_RATIO = 1L;
    private static final Long UPDATED_USER_RATIO = 2L;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .orderDate(DEFAULT_ORDER_DATE)
            .price(DEFAULT_PRICE)
            .finishDate(DEFAULT_FINISH_DATE)
            .userPhotoUrl(DEFAULT_USER_PHOTO_URL)
            .courierPhotoUrl(DEFAULT_COURIER_PHOTO_URL)
            .endOrderPhotoUrl(DEFAULT_END_ORDER_PHOTO_URL)
            .orderStartDate(DEFAULT_ORDER_START_DATE)
            .orderFinishDate(DEFAULT_ORDER_FINISH_DATE)
            .orderStatus(DEFAULT_ORDER_STATUS)
            .courierRatio(DEFAULT_COURIER_RATIO)
            .userRatio(DEFAULT_USER_RATIO);
        return order;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .orderDate(UPDATED_ORDER_DATE)
            .price(UPDATED_PRICE)
            .finishDate(UPDATED_FINISH_DATE)
            .userPhotoUrl(UPDATED_USER_PHOTO_URL)
            .courierPhotoUrl(UPDATED_COURIER_PHOTO_URL)
            .endOrderPhotoUrl(UPDATED_END_ORDER_PHOTO_URL)
            .orderStartDate(UPDATED_ORDER_START_DATE)
            .orderFinishDate(UPDATED_ORDER_FINISH_DATE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .courierRatio(UPDATED_COURIER_RATIO)
            .userRatio(UPDATED_USER_RATIO);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrder.getFinishDate()).isEqualTo(DEFAULT_FINISH_DATE);
        assertThat(testOrder.getUserPhotoUrl()).isEqualTo(DEFAULT_USER_PHOTO_URL);
        assertThat(testOrder.getCourierPhotoUrl()).isEqualTo(DEFAULT_COURIER_PHOTO_URL);
        assertThat(testOrder.getEndOrderPhotoUrl()).isEqualTo(DEFAULT_END_ORDER_PHOTO_URL);
        assertThat(testOrder.getOrderStartDate()).isEqualTo(DEFAULT_ORDER_START_DATE);
        assertThat(testOrder.getOrderFinishDate()).isEqualTo(DEFAULT_ORDER_FINISH_DATE);
        assertThat(testOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testOrder.getCourierRatio()).isEqualTo(DEFAULT_COURIER_RATIO);
        assertThat(testOrder.getUserRatio()).isEqualTo(DEFAULT_USER_RATIO);
    }

    @Test
    @Transactional
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].userPhotoUrl").value(hasItem(DEFAULT_USER_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].courierPhotoUrl").value(hasItem(DEFAULT_COURIER_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].endOrderPhotoUrl").value(hasItem(DEFAULT_END_ORDER_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].orderStartDate").value(hasItem(DEFAULT_ORDER_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderFinishDate").value(hasItem(DEFAULT_ORDER_FINISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].courierRatio").value(hasItem(DEFAULT_COURIER_RATIO.intValue())))
            .andExpect(jsonPath("$.[*].userRatio").value(hasItem(DEFAULT_USER_RATIO.intValue())));
    }
    
    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.finishDate").value(DEFAULT_FINISH_DATE.toString()))
            .andExpect(jsonPath("$.userPhotoUrl").value(DEFAULT_USER_PHOTO_URL))
            .andExpect(jsonPath("$.courierPhotoUrl").value(DEFAULT_COURIER_PHOTO_URL))
            .andExpect(jsonPath("$.endOrderPhotoUrl").value(DEFAULT_END_ORDER_PHOTO_URL))
            .andExpect(jsonPath("$.orderStartDate").value(DEFAULT_ORDER_START_DATE.toString()))
            .andExpect(jsonPath("$.orderFinishDate").value(DEFAULT_ORDER_FINISH_DATE.toString()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.courierRatio").value(DEFAULT_COURIER_RATIO.intValue()))
            .andExpect(jsonPath("$.userRatio").value(DEFAULT_USER_RATIO.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderService.save(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .orderDate(UPDATED_ORDER_DATE)
            .price(UPDATED_PRICE)
            .finishDate(UPDATED_FINISH_DATE)
            .userPhotoUrl(UPDATED_USER_PHOTO_URL)
            .courierPhotoUrl(UPDATED_COURIER_PHOTO_URL)
            .endOrderPhotoUrl(UPDATED_END_ORDER_PHOTO_URL)
            .orderStartDate(UPDATED_ORDER_START_DATE)
            .orderFinishDate(UPDATED_ORDER_FINISH_DATE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .courierRatio(UPDATED_COURIER_RATIO)
            .userRatio(UPDATED_USER_RATIO);

        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrder)))
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrder.getFinishDate()).isEqualTo(UPDATED_FINISH_DATE);
        assertThat(testOrder.getUserPhotoUrl()).isEqualTo(UPDATED_USER_PHOTO_URL);
        assertThat(testOrder.getCourierPhotoUrl()).isEqualTo(UPDATED_COURIER_PHOTO_URL);
        assertThat(testOrder.getEndOrderPhotoUrl()).isEqualTo(UPDATED_END_ORDER_PHOTO_URL);
        assertThat(testOrder.getOrderStartDate()).isEqualTo(UPDATED_ORDER_START_DATE);
        assertThat(testOrder.getOrderFinishDate()).isEqualTo(UPDATED_ORDER_FINISH_DATE);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrder.getCourierRatio()).isEqualTo(UPDATED_COURIER_RATIO);
        assertThat(testOrder.getUserRatio()).isEqualTo(UPDATED_USER_RATIO);
    }

    @Test
    @Transactional
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderService.save(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
