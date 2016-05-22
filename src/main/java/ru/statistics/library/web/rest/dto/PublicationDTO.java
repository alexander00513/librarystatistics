package ru.statistics.library.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ru.statistics.library.domain.enumeration.PublicationType;

/**
 * A DTO for the Publication entity.
 */
public class PublicationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String name;

    @NotNull
    @Size(max = 256)
    private String author;

    @Size(max = 128)
    private String isbn;

    @NotNull
    private PublicationType type;


    private Long libraryId;
    
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

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PublicationDTO publicationDTO = (PublicationDTO) o;

        if ( ! Objects.equals(id, publicationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PublicationDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", author='" + author + "'" +
            ", isbn='" + isbn + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
