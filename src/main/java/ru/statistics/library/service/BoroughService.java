package ru.statistics.library.service;

import ru.statistics.library.domain.Borough;
import ru.statistics.library.web.rest.dto.BoroughDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Borough.
 */
public interface BoroughService {

    /**
     * Save a borough.
     * 
     * @param boroughDTO the entity to save
     * @return the persisted entity
     */
    BoroughDTO save(BoroughDTO boroughDTO);

    /**
     *  Get all the boroughs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Borough> findAll(Pageable pageable);

    /**
     *  Get the "id" borough.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    BoroughDTO findOne(Long id);

    /**
     *  Delete the "id" borough.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the borough corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Borough> search(String query, Pageable pageable);
}
