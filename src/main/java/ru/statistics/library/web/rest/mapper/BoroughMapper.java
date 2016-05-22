package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.BoroughDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Borough and its DTO BoroughDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BoroughMapper {

    BoroughDTO boroughToBoroughDTO(Borough borough);

    List<BoroughDTO> boroughsToBoroughDTOs(List<Borough> boroughs);

    @Mapping(target = "libraries", ignore = true)
    Borough boroughDTOToBorough(BoroughDTO boroughDTO);

    List<Borough> boroughDTOsToBoroughs(List<BoroughDTO> boroughDTOs);
}
