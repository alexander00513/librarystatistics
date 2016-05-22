package ru.statistics.library.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the PublicationsRequest entity.
 */
public class PublicationsRequestDTO implements Serializable {

    private Long id;

    private ZonedDateTime fromDate;

    private ZonedDateTime toDate;


    private Long libraryId;
    
    private Long personId;
    
    private Long publicationId;
    
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

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PublicationsRequestDTO publicationsRequestDTO = (PublicationsRequestDTO) o;

        if ( ! Objects.equals(id, publicationsRequestDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PublicationsRequestDTO{" +
            "id=" + id +
            ", fromDate='" + fromDate + "'" +
            ", toDate='" + toDate + "'" +
            '}';
    }
}
