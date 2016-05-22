package ru.statistics.library.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ru.statistics.library.domain.enumeration.LibraryType;

/**
 * A DTO for the Library entity.
 */
public class LibraryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String name;

    @NotNull
    @Size(max = 256)
    private String description;

    @NotNull
    private LibraryType type;

    private Boolean internetAccess;


    private Long boroughId;
    private Long boroughName;

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
    public Boolean getInternetAccess() {
        return internetAccess;
    }

    public void setInternetAccess(Boolean internetAccess) {
        this.internetAccess = internetAccess;
    }

    public Long getBoroughId() {
        return boroughId;
    }

    public void setBoroughId(Long boroughId) {
        this.boroughId = boroughId;
    }

    public Long getBoroughName() {
        return boroughName;
    }

    public void setBoroughName(Long boroughName) {
        this.boroughName = boroughName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LibraryDTO libraryDTO = (LibraryDTO) o;

        if ( ! Objects.equals(id, libraryDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LibraryDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", type=" + type +
            ", internetAccess=" + internetAccess +
            ", boroughId=" + boroughId +
            ", boroughName=" + boroughName +
            '}';
    }
}
