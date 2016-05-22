package ru.statistics.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ru.statistics.library.domain.enumeration.LibraryType;

/**
 * A Library.
 */
@Entity
@Table(name = "library")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "library")
public class Library implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 128)
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @NotNull
    @Size(max = 256)
    @Column(name = "description", length = 256, nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LibraryType type;

    @Column(name = "internet_access")
    private Boolean internetAccess;

    @ManyToOne
    private Borough borough;

    @OneToMany(mappedBy = "library")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Equipment> equipments = new HashSet<>();

    @OneToMany(mappedBy = "library")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Publication> publications = new HashSet<>();

    @OneToMany(mappedBy = "library")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Subscription> subscriptions = new HashSet<>();

    @OneToMany(mappedBy = "library")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PublicationsRequest> publicationsRequests = new HashSet<>();

    @OneToMany(mappedBy = "library")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "library")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Website> websites = new HashSet<>();

    @ManyToMany(mappedBy = "libraries")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Person> people = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LibraryType getType() {
        return type;
    }

    public void setType(LibraryType type) {
        this.type = type;
    }

    public Boolean isInternetAccess() {
        return internetAccess;
    }

    public void setInternetAccess(Boolean internetAccess) {
        this.internetAccess = internetAccess;
    }

    public Borough getBorough() {
        return borough;
    }

    public void setBorough(Borough borough) {
        this.borough = borough;
    }

    public Set<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Set<Equipment> equipment) {
        this.equipments = equipment;
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<PublicationsRequest> getPublicationsRequests() {
        return publicationsRequests;
    }

    public void setPublicationsRequests(Set<PublicationsRequest> publicationsRequests) {
        this.publicationsRequests = publicationsRequests;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Website> getWebsites() {
        return websites;
    }

    public void setWebsites(Set<Website> websites) {
        this.websites = websites;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Library library = (Library) o;
        if(library.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, library.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Library{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", type='" + type + "'" +
            ", internetAccess='" + internetAccess + "'" +
            '}';
    }
}
