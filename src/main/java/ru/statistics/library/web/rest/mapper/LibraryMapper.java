package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.LibraryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Library and its DTO LibraryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LibraryMapper {

    @Mapping(source = "borough.id", target = "boroughId")
    @Mapping(source = "borough.name", target = "boroughName")
    LibraryDTO libraryToLibraryDTO(Library library);
    List<LibraryDTO> librariesToLibraryDTOs(List<Library> lraries);

    @Mapping(source = "boroughId", target = "borough")
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "publications", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "publicationsRequests", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "websites", ignore = true)
    @Mapping(target = "people", ignore = true)
    Library libraryDTOToLibrary(LibraryDTO libraryDTO);

    List<Library> libraryDTOsToLibraries(List<LibraryDTO> libraryDTOs);

    default Borough boroughFromId(Long id) {
        if (id == null) {
            return null;
        }
        Borough borough = new Borough();
        borough.setId(id);
        return borough;
    }
}
