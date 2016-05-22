package ru.statistics.library.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.statistics.library.domain.PublicationsRequest;
import ru.statistics.library.service.PublicationsRequestService;
import ru.statistics.library.web.rest.util.HeaderUtil;
import ru.statistics.library.web.rest.util.PaginationUtil;
import ru.statistics.library.web.rest.dto.PublicationsRequestDTO;
import ru.statistics.library.web.rest.mapper.PublicationsRequestMapper;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PublicationsRequest.
 */
@RestController
@RequestMapping("/api")
public class PublicationsRequestResource {

    private final Logger log = LoggerFactory.getLogger(PublicationsRequestResource.class);
        
    @Inject
    private PublicationsRequestService publicationsRequestService;
    
    @Inject
    private PublicationsRequestMapper publicationsRequestMapper;
    
    /**
     * POST  /publications-requests : Create a new publicationsRequest.
     *
     * @param publicationsRequestDTO the publicationsRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new publicationsRequestDTO, or with status 400 (Bad Request) if the publicationsRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/publications-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PublicationsRequestDTO> createPublicationsRequest(@RequestBody PublicationsRequestDTO publicationsRequestDTO) throws URISyntaxException {
        log.debug("REST request to save PublicationsRequest : {}", publicationsRequestDTO);
        if (publicationsRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("publicationsRequest", "idexists", "A new publicationsRequest cannot already have an ID")).body(null);
        }
        PublicationsRequestDTO result = publicationsRequestService.save(publicationsRequestDTO);
        return ResponseEntity.created(new URI("/api/publications-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("publicationsRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /publications-requests : Updates an existing publicationsRequest.
     *
     * @param publicationsRequestDTO the publicationsRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated publicationsRequestDTO,
     * or with status 400 (Bad Request) if the publicationsRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the publicationsRequestDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/publications-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PublicationsRequestDTO> updatePublicationsRequest(@RequestBody PublicationsRequestDTO publicationsRequestDTO) throws URISyntaxException {
        log.debug("REST request to update PublicationsRequest : {}", publicationsRequestDTO);
        if (publicationsRequestDTO.getId() == null) {
            return createPublicationsRequest(publicationsRequestDTO);
        }
        PublicationsRequestDTO result = publicationsRequestService.save(publicationsRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("publicationsRequest", publicationsRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /publications-requests : get all the publicationsRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of publicationsRequests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/publications-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PublicationsRequestDTO>> getAllPublicationsRequests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PublicationsRequests");
        Page<PublicationsRequest> page = publicationsRequestService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/publications-requests");
        return new ResponseEntity<>(publicationsRequestMapper.publicationsRequestsToPublicationsRequestDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /publications-requests/:id : get the "id" publicationsRequest.
     *
     * @param id the id of the publicationsRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the publicationsRequestDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/publications-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PublicationsRequestDTO> getPublicationsRequest(@PathVariable Long id) {
        log.debug("REST request to get PublicationsRequest : {}", id);
        PublicationsRequestDTO publicationsRequestDTO = publicationsRequestService.findOne(id);
        return Optional.ofNullable(publicationsRequestDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /publications-requests/:id : delete the "id" publicationsRequest.
     *
     * @param id the id of the publicationsRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/publications-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePublicationsRequest(@PathVariable Long id) {
        log.debug("REST request to delete PublicationsRequest : {}", id);
        publicationsRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("publicationsRequest", id.toString())).build();
    }

    /**
     * SEARCH  /_search/publications-requests?query=:query : search for the publicationsRequest corresponding
     * to the query.
     *
     * @param query the query of the publicationsRequest search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/publications-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PublicationsRequestDTO>> searchPublicationsRequests(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PublicationsRequests for query {}", query);
        Page<PublicationsRequest> page = publicationsRequestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/publications-requests");
        return new ResponseEntity<>(publicationsRequestMapper.publicationsRequestsToPublicationsRequestDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
