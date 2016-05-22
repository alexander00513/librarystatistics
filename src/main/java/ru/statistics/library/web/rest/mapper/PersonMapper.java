package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.PersonDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Person and its DTO PersonDTO.
 */
@Mapper(componentModel = "spring", uses = {LibraryMapper.class, EventMapper.class, })
public interface PersonMapper {

    PersonDTO personToPersonDTO(Person person);

    List<PersonDTO> peopleToPersonDTOs(List<Person> people);

    Person personDTOToPerson(PersonDTO personDTO);

    List<Person> personDTOsToPeople(List<PersonDTO> personDTOs);

    default Library libraryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Library library = new Library();
        library.setId(id);
        return library;
    }

    default Event eventFromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
