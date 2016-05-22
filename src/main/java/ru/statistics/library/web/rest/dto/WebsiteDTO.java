package ru.statistics.library.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ru.statistics.library.domain.enumeration.WebsiteType;

/**
 * A DTO for the Website entity.
 */
public class WebsiteDTO implements Serializable {

    private Long id;

    @NotNull
    private String url;

    private Long visits;

    @NotNull
    private WebsiteType type;


    private Long libraryId;
    
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

        WebsiteDTO websiteDTO = (WebsiteDTO) o;

        if ( ! Objects.equals(id, websiteDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WebsiteDTO{" +
            "id=" + id +
            ", url='" + url + "'" +
            ", visits='" + visits + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
