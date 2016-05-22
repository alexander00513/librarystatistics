package ru.statistics.library.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Borough entity.
 */
public class BoroughDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 32)
    private String name;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoroughDTO boroughDTO = (BoroughDTO) o;

        if ( ! Objects.equals(id, boroughDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BoroughDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
