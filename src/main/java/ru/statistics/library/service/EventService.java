package ru.statistics.library.service;

import ru.statistics.library.domain.Event;
import ru.statistics.library.web.rest.dto.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Event.
 */
public interface EventService {

    /**
     * Save a event.
     * 
     * @param eventDTO the entity to save
     * @return the persisted entity
     */
    EventDTO save(EventDTO eventDTO);

    /**
     *  Get all the events.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Event> findAll(Pageable pageable);

    /**
     *  Get the "id" event.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    EventDTO findOne(Long id);

    /**
     *  Delete the "id" event.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the event corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Event> search(String query, Pageable pageable);
}
