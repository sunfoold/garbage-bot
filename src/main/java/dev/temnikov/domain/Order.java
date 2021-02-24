package dev.temnikov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

import dev.temnikov.domain.enumeration.OrderStatus;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "price")
    private Long price;

    @Column(name = "finish_date")
    private Instant finishDate;

    @Column(name = "user_photo_url")
    private String userPhotoUrl;

    @Column(name = "courier_photo_url")
    private String courierPhotoUrl;

    @Column(name = "end_order_photo_url")
    private String endOrderPhotoUrl;

    @Column(name = "order_start_date")
    private Instant orderStartDate;

    @Column(name = "order_finish_date")
    private Instant orderFinishDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "courier_ratio")
    private Long courierRatio;

    @Column(name = "user_ratio")
    private Long userRatio;

    @OneToOne
    @JoinColumn(unique = true)
    private Garbage garbage;

    @ManyToOne
    @JsonIgnoreProperties(value = "orders", allowSetters = true)
    private AppUser user;

    @ManyToOne
    @JsonIgnoreProperties(value = "orders", allowSetters = true)
    private Courier courier;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public Order orderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Long getPrice() {
        return price;
    }

    public Order price(Long price) {
        this.price = price;
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Instant getFinishDate() {
        return finishDate;
    }

    public Order finishDate(Instant finishDate) {
        this.finishDate = finishDate;
        return this;
    }

    public void setFinishDate(Instant finishDate) {
        this.finishDate = finishDate;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public Order userPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
        return this;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getCourierPhotoUrl() {
        return courierPhotoUrl;
    }

    public Order courierPhotoUrl(String courierPhotoUrl) {
        this.courierPhotoUrl = courierPhotoUrl;
        return this;
    }

    public void setCourierPhotoUrl(String courierPhotoUrl) {
        this.courierPhotoUrl = courierPhotoUrl;
    }

    public String getEndOrderPhotoUrl() {
        return endOrderPhotoUrl;
    }

    public Order endOrderPhotoUrl(String endOrderPhotoUrl) {
        this.endOrderPhotoUrl = endOrderPhotoUrl;
        return this;
    }

    public void setEndOrderPhotoUrl(String endOrderPhotoUrl) {
        this.endOrderPhotoUrl = endOrderPhotoUrl;
    }

    public Instant getOrderStartDate() {
        return orderStartDate;
    }

    public Order orderStartDate(Instant orderStartDate) {
        this.orderStartDate = orderStartDate;
        return this;
    }

    public void setOrderStartDate(Instant orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    public Instant getOrderFinishDate() {
        return orderFinishDate;
    }

    public Order orderFinishDate(Instant orderFinishDate) {
        this.orderFinishDate = orderFinishDate;
        return this;
    }

    public void setOrderFinishDate(Instant orderFinishDate) {
        this.orderFinishDate = orderFinishDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getCourierRatio() {
        return courierRatio;
    }

    public Order courierRatio(Long courierRatio) {
        this.courierRatio = courierRatio;
        return this;
    }

    public void setCourierRatio(Long courierRatio) {
        this.courierRatio = courierRatio;
    }

    public Long getUserRatio() {
        return userRatio;
    }

    public Order userRatio(Long userRatio) {
        this.userRatio = userRatio;
        return this;
    }

    public void setUserRatio(Long userRatio) {
        this.userRatio = userRatio;
    }

    public Garbage getGarbage() {
        return garbage;
    }

    public Order garbage(Garbage garbage) {
        this.garbage = garbage;
        return this;
    }

    public void setGarbage(Garbage garbage) {
        this.garbage = garbage;
    }

    public AppUser getUser() {
        return user;
    }

    public Order user(AppUser appUser) {
        this.user = appUser;
        return this;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public Courier getCourier() {
        return courier;
    }

    public Order courier(Courier courier) {
        this.courier = courier;
        return this;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", price=" + getPrice() +
            ", finishDate='" + getFinishDate() + "'" +
            ", userPhotoUrl='" + getUserPhotoUrl() + "'" +
            ", courierPhotoUrl='" + getCourierPhotoUrl() + "'" +
            ", endOrderPhotoUrl='" + getEndOrderPhotoUrl() + "'" +
            ", orderStartDate='" + getOrderStartDate() + "'" +
            ", orderFinishDate='" + getOrderFinishDate() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", courierRatio=" + getCourierRatio() +
            ", userRatio=" + getUserRatio() +
            "}";
    }
}
