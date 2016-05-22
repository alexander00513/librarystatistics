package ru.statistics.library.service.impl;

import ru.statistics.library.service.EventService;
import ru.statistics.library.domain.Event;
import ru.statistics.library.repository.EventRepository;
import ru.statistics.library.repository.search.EventSearchRepository;
import ru.statistics.library.web.rest.dto.EventDTO;
import ru.statistics.library.web.rest.mapper.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService{

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
    
    @Inject
    private EventRepository eventRepository;
    
    @Inject
    private EventMapper eventMapper;
    
    @Inject
    private EventSearchRepository eventSearchRepository;
    
    /**
     * Save a event.
     * 
     * @param eventDTO the entity to save
     * @return the persisted entity
     */
    public EventDTO save(EventDTO eventDTO) {
        log.debug("Request to save Event : {}", eventDTO);
        Event event = eventMapper.eventDTOToEvent(eventDTO);
        event = eventRepository.save(event);
        EventDTO result = eventMapper.eventToEventDTO(event);
        eventSearchRepository.save(event);
        return result;
    }

    /**
     *  Get all the events.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Event> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        Page<Event> result = eventRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one event by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EventDTO findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        Event event = eventRepository.findOne(id);
        EventDTO eventDTO = eventMapper.eventToEventDTO(event);
        return eventDTO;
    }

    /**
     *  Delete the  event by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.delete(id);
        eventSearchRepository.delete(id);
    }

    /**
     * Search for the event corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Event> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Events for query {}", query);
        return eventSearchRepository.search(queryStringQuery(query), pageable);
    }
}
