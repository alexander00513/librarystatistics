package ru.statistics.library.service.impl;

import ru.statistics.library.service.PublicationsRequestService;
import ru.statistics.library.domain.PublicationsRequest;
import ru.statistics.library.repository.PublicationsRequestRepository;
import ru.statistics.library.repository.search.PublicationsRequestSearchRepository;
import ru.statistics.library.web.rest.dto.PublicationsRequestDTO;
import ru.statistics.library.web.rest.mapper.PublicationsRequestMapper;
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
 * Service Implementation for managing PublicationsRequest.
 */
@Service
@Transactional
public class PublicationsRequestServiceImpl implements PublicationsRequestService{

    private final Logger log = LoggerFactory.getLogger(PublicationsRequestServiceImpl.class);
    
    @Inject
    private PublicationsRequestRepository publicationsRequestRepository;
    
    @Inject
    private PublicationsRequestMapper publicationsRequestMapper;
    
    @Inject
    private PublicationsRequestSearchRepository publicationsRequestSearchRepository;
    
    /**
     * Save a publicationsRequest.
     * 
     * @param publicationsRequestDTO the entity to save
     * @return the persisted entity
     */
    public PublicationsRequestDTO save(PublicationsRequestDTO publicationsRequestDTO) {
        log.debug("Request to save PublicationsRequest : {}", publicationsRequestDTO);
        PublicationsRequest publicationsRequest = publicationsRequestMapper.publicationsRequestDTOToPublicationsRequest(publicationsRequestDTO);
        publicationsRequest = publicationsRequestRepository.save(publicationsRequest);
        PublicationsRequestDTO result = publicationsRequestMapper.publicationsRequestToPublicationsRequestDTO(publicationsRequest);
        publicationsRequestSearchRepository.save(publicationsRequest);
        return result;
    }

    /**
     *  Get all the publicationsRequests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PublicationsRequest> findAll(Pageable pageable) {
        log.debug("Request to get all PublicationsRequests");
        Page<PublicationsRequest> result = publicationsRequestRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one publicationsRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PublicationsRequestDTO findOne(Long id) {
        log.debug("Request to get PublicationsRequest : {}", id);
        PublicationsRequest publicationsRequest = publicationsRequestRepository.findOne(id);
        PublicationsRequestDTO publicationsRequestDTO = publicationsRequestMapper.publicationsRequestToPublicationsRequestDTO(publicationsRequest);
        return publicationsRequestDTO;
    }

    /**
     *  Delete the  publicationsRequest by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PublicationsRequest : {}", id);
        publicationsRequestRepository.delete(id);
        publicationsRequestSearchRepository.delete(id);
    }

    /**
     * Search for the publicationsRequest corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PublicationsRequest> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PublicationsRequests for query {}", query);
        return publicationsRequestSearchRepository.search(queryStringQuery(query), pageable);
    }
}
