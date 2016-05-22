package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.EventDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Event and its DTO EventDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventMapper {

    @Mapping(source = "library.id", target = "libraryId")
    EventDTO eventToEventDTO(Event event);

    List<EventDTO> eventsToEventDTOs(List<Event> events);

    @Mapping(source = "libraryId", target = "library")
    @Mapping(target = "people", ignore = true)
    Event eventDTOToEvent(EventDTO eventDTO);

    List<Event> eventDTOsToEvents(List<EventDTO> eventDTOs);

    default Library libraryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Library library = new Library();
        library.setId(id);
        return library;
    }
}
