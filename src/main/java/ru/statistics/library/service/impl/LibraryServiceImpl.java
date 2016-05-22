package ru.statistics.library.service.impl;

import ru.statistics.library.service.LibraryService;
import ru.statistics.library.domain.Library;
import ru.statistics.library.repository.LibraryRepository;
import ru.statistics.library.repository.search.LibrarySearchRepository;
import ru.statistics.library.web.rest.dto.LibraryDTO;
import ru.statistics.library.web.rest.mapper.LibraryMapper;
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
 * Service Implementation for managing Library.
 */
@Service
@Transactional
public class LibraryServiceImpl implements LibraryService{

    private final Logger log = LoggerFactory.getLogger(LibraryServiceImpl.class);
    
    @Inject
    private LibraryRepository libraryRepository;
    
    @Inject
    private LibraryMapper libraryMapper;
    
    @Inject
    private LibrarySearchRepository librarySearchRepository;
    
    /**
     * Save a library.
     * 
     * @param libraryDTO the entity to save
     * @return the persisted entity
     */
    public LibraryDTO save(LibraryDTO libraryDTO) {
        log.debug("Request to save Library : {}", libraryDTO);
        Library library = libraryMapper.libraryDTOToLibrary(libraryDTO);
        library = libraryRepository.save(library);
        LibraryDTO result = libraryMapper.libraryToLibraryDTO(library);
        librarySearchRepository.save(library);
        return result;
    }

    /**
     *  Get all the libraries.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Library> findAll(Pageable pageable) {
        log.debug("Request to get all Libraries");
        Page<Library> result = libraryRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one library by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public LibraryDTO findOne(Long id) {
        log.debug("Request to get Library : {}", id);
        Library library = libraryRepository.findOne(id);
        LibraryDTO libraryDTO = libraryMapper.libraryToLibraryDTO(library);
        return libraryDTO;
    }

    /**
     *  Delete the  library by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Library : {}", id);
        libraryRepository.delete(id);
        librarySearchRepository.delete(id);
    }

    /**
     * Search for the library corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Library> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Libraries for query {}", query);
        return librarySearchRepository.search(queryStringQuery(query), pageable);
    }
}
