package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.PublicationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Publication and its DTO PublicationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PublicationMapper {

    @Mapping(source = "library.id", target = "libraryId")
    PublicationDTO publicationToPublicationDTO(Publication publication);

    List<PublicationDTO> publicationsToPublicationDTOs(List<Publication> publications);

    @Mapping(source = "libraryId", target = "library")
    Publication publicationDTOToPublication(PublicationDTO publicationDTO);

    List<Publication> publicationDTOsToPublications(List<PublicationDTO> publicationDTOs);

    default Library libraryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Library library = new Library();
        library.setId(id);
        return library;
    }
}
