package ru.statistics.library.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Subscription.
 */
@Entity
@Table(name = "subscription")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "subscription")
public class Subscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "from_date")
    private ZonedDateTime fromDate;

    @Column(name = "to_date")
    private ZonedDateTime toDate;

    @ManyToOne
    private Library library;

    @OneToOne
    @JoinColumn(unique = true)
    private Person person;

    @OneToOne
    @JoinColumn(unique = true)
    private Publication publication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subscription subscription = (Subscription) o;
        if(subscription.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, subscription.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Subscription{" +
            "id=" + id +
            ", fromDate='" + fromDate + "'" +
            ", toDate='" + toDate + "'" +
            '}';
    }
}
