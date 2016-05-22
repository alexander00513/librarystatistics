package ru.statistics.library.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.statistics.library.domain.Website;
import ru.statistics.library.service.WebsiteService;
import ru.statistics.library.web.rest.util.HeaderUtil;
import ru.statistics.library.web.rest.util.PaginationUtil;
import ru.statistics.library.web.rest.dto.WebsiteDTO;
import ru.statistics.library.web.rest.mapper.WebsiteMapper;
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
 * REST controller for managing Website.
 */
@RestController
@RequestMapping("/api")
public class WebsiteResource {

    private final Logger log = LoggerFactory.getLogger(WebsiteResource.class);
        
    @Inject
    private WebsiteService websiteService;
    
    @Inject
    private WebsiteMapper websiteMapper;
    
    /**
     * POST  /websites : Create a new website.
     *
     * @param websiteDTO the websiteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new websiteDTO, or with status 400 (Bad Request) if the website has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/websites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WebsiteDTO> createWebsite(@Valid @RequestBody WebsiteDTO websiteDTO) throws URISyntaxException {
        log.debug("REST request to save Website : {}", websiteDTO);
        if (websiteDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("website", "idexists", "A new website cannot already have an ID")).body(null);
        }
        WebsiteDTO result = websiteService.save(websiteDTO);
        return ResponseEntity.created(new URI("/api/websites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("website", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /websites : Updates an existing website.
     *
     * @param websiteDTO the websiteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated websiteDTO,
     * or with status 400 (Bad Request) if the websiteDTO is not valid,
     * or with status 500 (Internal Server Error) if the websiteDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/websites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WebsiteDTO> updateWebsite(@Valid @RequestBody WebsiteDTO websiteDTO) throws URISyntaxException {
        log.debug("REST request to update Website : {}", websiteDTO);
        if (websiteDTO.getId() == null) {
            return createWebsite(websiteDTO);
        }
        WebsiteDTO result = websiteService.save(websiteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("website", websiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /websites : get all the websites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of websites in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/websites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WebsiteDTO>> getAllWebsites(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Websites");
        Page<Website> page = websiteService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/websites");
        return new ResponseEntity<>(websiteMapper.websitesToWebsiteDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /websites/:id : get the "id" website.
     *
     * @param id the id of the websiteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the websiteDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/websites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WebsiteDTO> getWebsite(@PathVariable Long id) {
        log.debug("REST request to get Website : {}", id);
        WebsiteDTO websiteDTO = websiteService.findOne(id);
        return Optional.ofNullable(websiteDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /websites/:id : delete the "id" website.
     *
     * @param id the id of the websiteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/websites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWebsite(@PathVariable Long id) {
        log.debug("REST request to delete Website : {}", id);
        websiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("website", id.toString())).build();
    }

    /**
     * SEARCH  /_search/websites?query=:query : search for the website corresponding
     * to the query.
     *
     * @param query the query of the website search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/websites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WebsiteDTO>> searchWebsites(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Websites for query {}", query);
        Page<Website> page = websiteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/websites");
        return new ResponseEntity<>(websiteMapper.websitesToWebsiteDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
