package dev.temnikov.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Shift.
 */
@Entity
@Table(name = "shift")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Shift implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "shift_plan_start_date")
    private Instant shiftPlanStartDate;

    @Column(name = "shift_fact_start_date")
    private Instant shiftFactStartDate;

    @Column(name = "shift_plan_end_date")
    private Instant shiftPlanEndDate;

    @Column(name = "shift_fact_end_date")
    private Instant shiftFactEndDate;

    @Column(name = "prepaid")
    private Boolean prepaid;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getShiftPlanStartDate() {
        return shiftPlanStartDate;
    }

    public Shift shiftPlanStartDate(Instant shiftPlanStartDate) {
        this.shiftPlanStartDate = shiftPlanStartDate;
        return this;
    }

    public void setShiftPlanStartDate(Instant shiftPlanStartDate) {
        this.shiftPlanStartDate = shiftPlanStartDate;
    }

    public Instant getShiftFactStartDate() {
        return shiftFactStartDate;
    }

    public Shift shiftFactStartDate(Instant shiftFactStartDate) {
        this.shiftFactStartDate = shiftFactStartDate;
        return this;
    }

    public void setShiftFactStartDate(Instant shiftFactStartDate) {
        this.shiftFactStartDate = shiftFactStartDate;
    }

    public Instant getShiftPlanEndDate() {
        return shiftPlanEndDate;
    }

    public Shift shiftPlanEndDate(Instant shiftPlanEndDate) {
        this.shiftPlanEndDate = shiftPlanEndDate;
        return this;
    }

    public void setShiftPlanEndDate(Instant shiftPlanEndDate) {
        this.shiftPlanEndDate = shiftPlanEndDate;
    }

    public Instant getShiftFactEndDate() {
        return shiftFactEndDate;
    }

    public Shift shiftFactEndDate(Instant shiftFactEndDate) {
        this.shiftFactEndDate = shiftFactEndDate;
        return this;
    }

    public void setShiftFactEndDate(Instant shiftFactEndDate) {
        this.shiftFactEndDate = shiftFactEndDate;
    }

    public Boolean isPrepaid() {
        return prepaid;
    }

    public Shift prepaid(Boolean prepaid) {
        this.prepaid = prepaid;
        return this;
    }

    public void setPrepaid(Boolean prepaid) {
        this.prepaid = prepaid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shift)) {
            return false;
        }
        return id != null && id.equals(((Shift) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shift{" +
            "id=" + getId() +
            ", shiftPlanStartDate='" + getShiftPlanStartDate() + "'" +
            ", shiftFactStartDate='" + getShiftFactStartDate() + "'" +
            ", shiftPlanEndDate='" + getShiftPlanEndDate() + "'" +
            ", shiftFactEndDate='" + getShiftFactEndDate() + "'" +
            ", prepaid='" + isPrepaid() + "'" +
            "}";
    }
}
