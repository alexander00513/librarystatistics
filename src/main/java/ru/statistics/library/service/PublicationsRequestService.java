package ru.statistics.library.service;

import ru.statistics.library.domain.PublicationsRequest;
import ru.statistics.library.web.rest.dto.PublicationsRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing PublicationsRequest.
 */
public interface PublicationsRequestService {

    /**
     * Save a publicationsRequest.
     * 
     * @param publicationsRequestDTO the entity to save
     * @return the persisted entity
     */
    PublicationsRequestDTO save(PublicationsRequestDTO publicationsRequestDTO);

    /**
     *  Get all the publicationsRequests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PublicationsRequest> findAll(Pageable pageable);

    /**
     *  Get the "id" publicationsRequest.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PublicationsRequestDTO findOne(Long id);

    /**
     *  Delete the "id" publicationsRequest.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the publicationsRequest corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<PublicationsRequest> search(String query, Pageable pageable);
}
