package ru.statistics.library.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.statistics.library.domain.Borough;
import ru.statistics.library.service.BoroughService;
import ru.statistics.library.web.rest.util.HeaderUtil;
import ru.statistics.library.web.rest.util.PaginationUtil;
import ru.statistics.library.web.rest.dto.BoroughDTO;
import ru.statistics.library.web.rest.mapper.BoroughMapper;
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
 * REST controller for managing Borough.
 */
@RestController
@RequestMapping("/api")
public class BoroughResource {

    private final Logger log = LoggerFactory.getLogger(BoroughResource.class);
        
    @Inject
    private BoroughService boroughService;
    
    @Inject
    private BoroughMapper boroughMapper;
    
    /**
     * POST  /boroughs : Create a new borough.
     *
     * @param boroughDTO the boroughDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boroughDTO, or with status 400 (Bad Request) if the borough has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/boroughs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BoroughDTO> createBorough(@Valid @RequestBody BoroughDTO boroughDTO) throws URISyntaxException {
        log.debug("REST request to save Borough : {}", boroughDTO);
        if (boroughDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("borough", "idexists", "A new borough cannot already have an ID")).body(null);
        }
        BoroughDTO result = boroughService.save(boroughDTO);
        return ResponseEntity.created(new URI("/api/boroughs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("borough", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /boroughs : Updates an existing borough.
     *
     * @param boroughDTO the boroughDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boroughDTO,
     * or with status 400 (Bad Request) if the boroughDTO is not valid,
     * or with status 500 (Internal Server Error) if the boroughDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/boroughs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BoroughDTO> updateBorough(@Valid @RequestBody BoroughDTO boroughDTO) throws URISyntaxException {
        log.debug("REST request to update Borough : {}", boroughDTO);
        if (boroughDTO.getId() == null) {
            return createBorough(boroughDTO);
        }
        BoroughDTO result = boroughService.save(boroughDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("borough", boroughDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /boroughs : get all the boroughs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of boroughs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/boroughs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<BoroughDTO>> getAllBoroughs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Boroughs");
        Page<Borough> page = boroughService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/boroughs");
        return new ResponseEntity<>(boroughMapper.boroughsToBoroughDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /boroughs/:id : get the "id" borough.
     *
     * @param id the id of the boroughDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boroughDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/boroughs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BoroughDTO> getBorough(@PathVariable Long id) {
        log.debug("REST request to get Borough : {}", id);
        BoroughDTO boroughDTO = boroughService.findOne(id);
        return Optional.ofNullable(boroughDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /boroughs/:id : delete the "id" borough.
     *
     * @param id the id of the boroughDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/boroughs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBorough(@PathVariable Long id) {
        log.debug("REST request to delete Borough : {}", id);
        boroughService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("borough", id.toString())).build();
    }

    /**
     * SEARCH  /_search/boroughs?query=:query : search for the borough corresponding
     * to the query.
     *
     * @param query the query of the borough search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/boroughs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<BoroughDTO>> searchBoroughs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Boroughs for query {}", query);
        Page<Borough> page = boroughService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/boroughs");
        return new ResponseEntity<>(boroughMapper.boroughsToBoroughDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
