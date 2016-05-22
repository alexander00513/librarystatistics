package ru.statistics.library.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ru.statistics.library.domain.enumeration.EventType;

/**
 * A DTO for the Event entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String name;

    @Size(max = 512)
    private String description;

    @NotNull
    private EventType type;


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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
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

        EventDTO eventDTO = (EventDTO) o;

        if ( ! Objects.equals(id, eventDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
