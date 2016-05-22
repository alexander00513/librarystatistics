package ru.statistics.library.service;

import ru.statistics.library.domain.Library;
import ru.statistics.library.web.rest.dto.LibraryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Library.
 */
public interface LibraryService {

    /**
     * Save a library.
     * 
     * @param libraryDTO the entity to save
     * @return the persisted entity
     */
    LibraryDTO save(LibraryDTO libraryDTO);

    /**
     *  Get all the libraries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Library> findAll(Pageable pageable);

    /**
     *  Get the "id" library.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    LibraryDTO findOne(Long id);

    /**
     *  Delete the "id" library.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the library corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Library> search(String query, Pageable pageable);
}
