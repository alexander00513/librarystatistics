package ru.statistics.library.service.impl;

import ru.statistics.library.service.BoroughService;
import ru.statistics.library.domain.Borough;
import ru.statistics.library.repository.BoroughRepository;
import ru.statistics.library.repository.search.BoroughSearchRepository;
import ru.statistics.library.web.rest.dto.BoroughDTO;
import ru.statistics.library.web.rest.mapper.BoroughMapper;
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
 * Service Implementation for managing Borough.
 */
@Service
@Transactional
public class BoroughServiceImpl implements BoroughService{

    private final Logger log = LoggerFactory.getLogger(BoroughServiceImpl.class);
    
    @Inject
    private BoroughRepository boroughRepository;
    
    @Inject
    private BoroughMapper boroughMapper;
    
    @Inject
    private BoroughSearchRepository boroughSearchRepository;
    
    /**
     * Save a borough.
     * 
     * @param boroughDTO the entity to save
     * @return the persisted entity
     */
    public BoroughDTO save(BoroughDTO boroughDTO) {
        log.debug("Request to save Borough : {}", boroughDTO);
        Borough borough = boroughMapper.boroughDTOToBorough(boroughDTO);
        borough = boroughRepository.save(borough);
        BoroughDTO result = boroughMapper.boroughToBoroughDTO(borough);
        boroughSearchRepository.save(borough);
        return result;
    }

    /**
     *  Get all the boroughs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Borough> findAll(Pageable pageable) {
        log.debug("Request to get all Boroughs");
        Page<Borough> result = boroughRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one borough by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BoroughDTO findOne(Long id) {
        log.debug("Request to get Borough : {}", id);
        Borough borough = boroughRepository.findOne(id);
        BoroughDTO boroughDTO = boroughMapper.boroughToBoroughDTO(borough);
        return boroughDTO;
    }

    /**
     *  Delete the  borough by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Borough : {}", id);
        boroughRepository.delete(id);
        boroughSearchRepository.delete(id);
    }

    /**
     * Search for the borough corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Borough> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Boroughs for query {}", query);
        return boroughSearchRepository.search(queryStringQuery(query), pageable);
    }
}
