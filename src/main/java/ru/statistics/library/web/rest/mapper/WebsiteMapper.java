package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.WebsiteDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Website and its DTO WebsiteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WebsiteMapper {

    @Mapping(source = "library.id", target = "libraryId")
    WebsiteDTO websiteToWebsiteDTO(Website website);

    List<WebsiteDTO> websitesToWebsiteDTOs(List<Website> websites);

    @Mapping(source = "libraryId", target = "library")
    Website websiteDTOToWebsite(WebsiteDTO websiteDTO);

    List<Website> websiteDTOsToWebsites(List<WebsiteDTO> websiteDTOs);

    default Library libraryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Library library = new Library();
        library.setId(id);
        return library;
    }
}
