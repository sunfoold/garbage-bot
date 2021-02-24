package dev.temnikov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "street_building")
    private String streetBuilding;

    @Column(name = "level")
    private Long level;

    @Column(name = "apartment")
    private String apartment;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @ManyToOne
    @JsonIgnoreProperties(value = "addresses", allowSetters = true)
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public Address city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetBuilding() {
        return streetBuilding;
    }

    public Address streetBuilding(String streetBuilding) {
        this.streetBuilding = streetBuilding;
        return this;
    }

    public void setStreetBuilding(String streetBuilding) {
        this.streetBuilding = streetBuilding;
    }

    public Long getLevel() {
        return level;
    }

    public Address level(Long level) {
        this.level = level;
        return this;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getApartment() {
        return apartment;
    }

    public Address apartment(String apartment) {
        this.apartment = apartment;
        return this;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getLatitude() {
        return latitude;
    }

    public Address latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Address longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public Address appUser(AppUser appUser) {
        this.appUser = appUser;
        return this;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", streetBuilding='" + getStreetBuilding() + "'" +
            ", level=" + getLevel() +
            ", apartment='" + getApartment() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            "}";
    }
}
