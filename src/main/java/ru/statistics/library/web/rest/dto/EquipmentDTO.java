package ru.statistics.library.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ru.statistics.library.domain.enumeration.EquipmentType;

/**
 * A DTO for the Equipment entity.
 */
public class EquipmentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 128)
    private String uid;

    @Size(max = 512)
    private String decription;

    @NotNull
    private EquipmentType type;


    private Long libraryId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }
    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
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

        EquipmentDTO equipmentDTO = (EquipmentDTO) o;

        if ( ! Objects.equals(id, equipmentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EquipmentDTO{" +
            "id=" + id +
            ", uid='" + uid + "'" +
            ", decription='" + decription + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
