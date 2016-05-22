package ru.statistics.library.service;

import ru.statistics.library.domain.Publication;
import ru.statistics.library.web.rest.dto.PublicationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Publication.
 */
public interface PublicationService {

    /**
     * Save a publication.
     * 
     * @param publicationDTO the entity to save
     * @return the persisted entity
     */
    PublicationDTO save(PublicationDTO publicationDTO);

    /**
     *  Get all the publications.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Publication> findAll(Pageable pageable);

    /**
     *  Get the "id" publication.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    PublicationDTO findOne(Long id);

    /**
     *  Delete the "id" publication.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the publication corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Publication> search(String query, Pageable pageable);
}
