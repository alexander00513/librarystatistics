package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.EquipmentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Equipment and its DTO EquipmentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EquipmentMapper {

    @Mapping(source = "library.id", target = "libraryId")
    EquipmentDTO equipmentToEquipmentDTO(Equipment equipment);

    List<EquipmentDTO> equipmentToEquipmentDTOs(List<Equipment> equipment);

    @Mapping(source = "libraryId", target = "library")
    Equipment equipmentDTOToEquipment(EquipmentDTO equipmentDTO);

    List<Equipment> equipmentDTOsToEquipment(List<EquipmentDTO> equipmentDTOs);

    default Library libraryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Library library = new Library();
        library.setId(id);
        return library;
    }
}
