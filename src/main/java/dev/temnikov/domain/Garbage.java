package dev.temnikov.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Garbage.
 */
@Entity
@Table(name = "garbage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Garbage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "pockets")
    private Long pockets;

    @Column(name = "huge_things")
    private Long hugeThings;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPockets() {
        return pockets;
    }

    public Garbage pockets(Long pockets) {
        this.pockets = pockets;
        return this;
    }

    public void setPockets(Long pockets) {
        this.pockets = pockets;
    }

    public Long getHugeThings() {
        return hugeThings;
    }

    public Garbage hugeThings(Long hugeThings) {
        this.hugeThings = hugeThings;
        return this;
    }

    public void setHugeThings(Long hugeThings) {
        this.hugeThings = hugeThings;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Garbage)) {
            return false;
        }
        return id != null && id.equals(((Garbage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Garbage{" +
            "id=" + getId() +
            ", pockets=" + getPockets() +
            ", hugeThings=" + getHugeThings() +
            "}";
    }
}
