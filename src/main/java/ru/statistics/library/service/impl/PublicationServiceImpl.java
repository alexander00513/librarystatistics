package ru.statistics.library.service.impl;

import ru.statistics.library.service.PublicationService;
import ru.statistics.library.domain.Publication;
import ru.statistics.library.repository.PublicationRepository;
import ru.statistics.library.repository.search.PublicationSearchRepository;
import ru.statistics.library.web.rest.dto.PublicationDTO;
import ru.statistics.library.web.rest.mapper.PublicationMapper;
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
 * Service Implementation for managing Publication.
 */
@Service
@Transactional
public class PublicationServiceImpl implements PublicationService{

    private final Logger log = LoggerFactory.getLogger(PublicationServiceImpl.class);
    
    @Inject
    private PublicationRepository publicationRepository;
    
    @Inject
    private PublicationMapper publicationMapper;
    
    @Inject
    private PublicationSearchRepository publicationSearchRepository;
    
    /**
     * Save a publication.
     * 
     * @param publicationDTO the entity to save
     * @return the persisted entity
     */
    public PublicationDTO save(PublicationDTO publicationDTO) {
        log.debug("Request to save Publication : {}", publicationDTO);
        Publication publication = publicationMapper.publicationDTOToPublication(publicationDTO);
        publication = publicationRepository.save(publication);
        PublicationDTO result = publicationMapper.publicationToPublicationDTO(publication);
        publicationSearchRepository.save(publication);
        return result;
    }

    /**
     *  Get all the publications.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Publication> findAll(Pageable pageable) {
        log.debug("Request to get all Publications");
        Page<Publication> result = publicationRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one publication by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PublicationDTO findOne(Long id) {
        log.debug("Request to get Publication : {}", id);
        Publication publication = publicationRepository.findOne(id);
        PublicationDTO publicationDTO = publicationMapper.publicationToPublicationDTO(publication);
        return publicationDTO;
    }

    /**
     *  Delete the  publication by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Publication : {}", id);
        publicationRepository.delete(id);
        publicationSearchRepository.delete(id);
    }

    /**
     * Search for the publication corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Publication> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Publications for query {}", query);
        return publicationSearchRepository.search(queryStringQuery(query), pageable);
    }
}
