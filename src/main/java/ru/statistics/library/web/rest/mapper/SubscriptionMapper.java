package ru.statistics.library.web.rest.mapper;

import ru.statistics.library.domain.*;
import ru.statistics.library.web.rest.dto.SubscriptionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Subscription and its DTO SubscriptionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SubscriptionMapper {

    @Mapping(source = "library.id", target = "libraryId")
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "publication.id", target = "publicationId")
    SubscriptionDTO subscriptionToSubscriptionDTO(Subscription subscription);

    List<SubscriptionDTO> subscriptionsToSubscriptionDTOs(List<Subscription> subscriptions);

    @Mapping(source = "libraryId", target = "library")
    @Mapping(source = "personId", target = "person")
    @Mapping(source = "publicationId", target = "publication")
    Subscription subscriptionDTOToSubscription(SubscriptionDTO subscriptionDTO);

    List<Subscription> subscriptionDTOsToSubscriptions(List<SubscriptionDTO> subscriptionDTOs);

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
