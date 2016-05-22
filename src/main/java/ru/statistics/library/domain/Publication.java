package ru.statistics.library.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import ru.statistics.library.domain.enumeration.PublicationType;

/**
 * A Publication.
 */
@Entity
@Table(name = "publication")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "publication")
public class Publication implements Serializable {

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
    @Column(name = "author", length = 256, nullable = false)
    private String author;

    @Size(max = 128)
    @Column(name = "isbn", length = 128)
    private String isbn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PublicationType type;

    @ManyToOne
    private Library library;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public PublicationType getType() {
        return type;
    }

    public void setType(PublicationType type) {
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
        Publication publication = (Publication) o;
        if(publication.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, publication.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Publication{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", author='" + author + "'" +
            ", isbn='" + isbn + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
