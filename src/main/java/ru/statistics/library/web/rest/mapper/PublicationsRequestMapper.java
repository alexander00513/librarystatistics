package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.PublicationsRequestDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PublicationsRequest and its DTO PublicationsRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PublicationsRequestMapper {

    @Mapping(source = "library.id", target = "libraryId")
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "publication.id", target = "publicationId")
    PublicationsRequestDTO publicationsRequestToPublicationsRequestDTO(PublicationsRequest publicationsRequest);

    List<PublicationsRequestDTO> publicationsRequestsToPublicationsRequestDTOs(List<PublicationsRequest> publicationsRequests);

    @Mapping(source = "libraryId", target = "library")
    @Mapping(source = "personId", target = "person")
    @Mapping(source = "publicationId", target = "publication")
    PublicationsRequest publicationsRequestDTOToPublicationsRequest(PublicationsRequestDTO publicationsRequestDTO);

    List<PublicationsRequest> publicationsRequestDTOsToPublicationsRequests(List<PublicationsRequestDTO> publicationsRequestDTOs);

    default Library libraryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Library library = new Library();
        library.setId(id);
        return library;
    }

    default Person personFromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setId(id);
        return person;
    }

    default Publication publicationFromId(Long id) {
        if (id == null) {
            return null;
        }
        Publication publication = new Publication();
        publication.setId(id);
        return publication;
    }
}
