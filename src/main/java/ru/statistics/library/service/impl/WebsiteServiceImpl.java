package ru.statistics.library.service.impl;

import ru.statistics.library.service.WebsiteService;
import ru.statistics.library.domain.Website;
import ru.statistics.library.repository.WebsiteRepository;
import ru.statistics.library.repository.search.WebsiteSearchRepository;
import ru.statistics.library.web.rest.dto.WebsiteDTO;
import ru.statistics.library.web.rest.mapper.WebsiteMapper;
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
 * Service Implementation for managing Website.
 */
@Service
@Transactional
public class WebsiteServiceImpl implements WebsiteService{

    private final Logger log = LoggerFactory.getLogger(WebsiteServiceImpl.class);
    
    @Inject
    private WebsiteRepository websiteRepository;
    
    @Inject
    private WebsiteMapper websiteMapper;
    
    @Inject
    private WebsiteSearchRepository websiteSearchRepository;
    
    /**
     * Save a website.
     * 
     * @param websiteDTO the entity to save
     * @return the persisted entity
     */
    public WebsiteDTO save(WebsiteDTO websiteDTO) {
        log.debug("Request to save Website : {}", websiteDTO);
        Website website = websiteMapper.websiteDTOToWebsite(websiteDTO);
        website = websiteRepository.save(website);
        WebsiteDTO result = websiteMapper.websiteToWebsiteDTO(website);
        websiteSearchRepository.save(website);
        return result;
    }

    /**
     *  Get all the websites.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Website> findAll(Pageable pageable) {
        log.debug("Request to get all Websites");
        Page<Website> result = websiteRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one website by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public WebsiteDTO findOne(Long id) {
        log.debug("Request to get Website : {}", id);
        Website website = websiteRepository.findOne(id);
        WebsiteDTO websiteDTO = websiteMapper.websiteToWebsiteDTO(website);
        return websiteDTO;
    }

    /**
     *  Delete the  website by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Website : {}", id);
        websiteRepository.delete(id);
        websiteSearchRepository.delete(id);
    }

    /**
     * Search for the website corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Website> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Websites for query {}", query);
        return websiteSearchRepository.search(queryStringQuery(query), pageable);
    }
}
