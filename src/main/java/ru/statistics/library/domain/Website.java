package ru.statistics.library.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import ru.statistics.library.domain.enumeration.WebsiteType;

/**
 * A Website.
 */
@Entity
@Table(name = "website")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "website")
public class Website implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "visits")
    private Long visits;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private WebsiteType type;

    @ManyToOne
    private Library library;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getVisits() {
        return visits;
    }

    public void setVisits(Long visits) {
        this.visits = visits;
    }

    public WebsiteType getType() {
        return type;
    }

    public void setType(WebsiteType type) {
        this.type = type;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Website website = (Website) o;
        if(website.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, website.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Website{" +
            "id=" + id +
            ", url='" + url + "'" +
            ", visits='" + visits + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
