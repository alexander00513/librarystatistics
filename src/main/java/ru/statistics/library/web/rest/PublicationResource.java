package ru.statistics.library.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.statistics.library.domain.Publication;
import ru.statistics.library.service.PublicationService;
import ru.statistics.library.web.rest.util.HeaderUtil;
import ru.statistics.library.web.rest.util.PaginationUtil;
import ru.statistics.library.web.rest.dto.PublicationDTO;
import ru.statistics.library.web.rest.mapper.PublicationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Publication.
 */
@RestController
@RequestMapping("/api")
public class PublicationResource {

    private final Logger log = LoggerFactory.getLogger(PublicationResource.class);
        
    @Inject
    private PublicationService publicationService;
    
    @Inject
    private PublicationMapper publicationMapper;
    
    /**
     * POST  /publications : Create a new publication.
     *
     * @param publicationDTO the publicationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new publicationDTO, or with status 400 (Bad Request) if the publication has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/publications",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PublicationDTO> createPublication(@Valid @RequestBody PublicationDTO publicationDTO) throws URISyntaxException {
        log.debug("REST request to save Publication : {}", publicationDTO);
        if (publicationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("publication", "idexists", "A new publication cannot already have an ID")).body(null);
        }
        PublicationDTO result = publicationService.save(publicationDTO);
        return ResponseEntity.created(new URI("/api/publications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("publication", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /publications : Updates an existing publication.
     *
     * @param publicationDTO the publicationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated publicationDTO,
     * or with status 400 (Bad Request) if the publicationDTO is not valid,
     * or with status 500 (Internal Server Error) if the publicationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/publications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PublicationDTO> updatePublication(@Valid @RequestBody PublicationDTO publicationDTO) throws URISyntaxException {
        log.debug("REST request to update Publication : {}", publicationDTO);
        if (publicationDTO.getId() == null) {
            return createPublication(publicationDTO);
        }
        PublicationDTO result = publicationService.save(publicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("publication", publicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /publications : get all the publications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of publications in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/publications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PublicationDTO>> getAllPublications(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Publications");
        Page<Publication> page = publicationService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/publications");
        return new ResponseEntity<>(publicationMapper.publicationsToPublicationDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /publications/:id : get the "id" publication.
     *
     * @param id the id of the publicationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the publicationDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/publications/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PublicationDTO> getPublication(@PathVariable Long id) {
        log.debug("REST request to get Publication : {}", id);
        PublicationDTO publicationDTO = publicationService.findOne(id);
        return Optional.ofNullable(publicationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /publications/:id : delete the "id" publication.
     *
     * @param id the id of the publicationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/publications/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        log.debug("REST request to delete Publication : {}", id);
        publicationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("publication", id.toString())).build();
    }

    /**
     * SEARCH  /_search/publications?query=:query : search for the publication corresponding
     * to the query.
     *
     * @param query the query of the publication search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/publications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PublicationDTO>> searchPublications(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Publications for query {}", query);
        Page<Publication> page = publicationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/publications");
        return new ResponseEntity<>(publicationMapper.publicationsToPublicationDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
