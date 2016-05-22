package ru.statistics.library.service;

import ru.statistics.library.domain.Website;
import ru.statistics.library.web.rest.dto.WebsiteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Website.
 */
public interface WebsiteService {

    /**
     * Save a website.
     * 
     * @param websiteDTO the entity to save
     * @return the persisted entity
     */
    WebsiteDTO save(WebsiteDTO websiteDTO);

    /**
     *  Get all the websites.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Website> findAll(Pageable pageable);

    /**
     *  Get the "id" website.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    WebsiteDTO findOne(Long id);

    /**
     *  Delete the "id" website.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the website corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Website> search(String query, Pageable pageable);
}
