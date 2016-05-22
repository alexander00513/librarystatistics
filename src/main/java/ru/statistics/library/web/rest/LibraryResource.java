package ru.statistics.library.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.statistics.library.domain.Library;
import ru.statistics.library.service.LibraryService;
import ru.statistics.library.web.rest.util.HeaderUtil;
import ru.statistics.library.web.rest.util.PaginationUtil;
import ru.statistics.library.web.rest.dto.LibraryDTO;
import ru.statistics.library.web.rest.mapper.LibraryMapper;
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
 * REST controller for managing Library.
 */
@RestController
@RequestMapping("/api")
public class LibraryResource {

    private final Logger log = LoggerFactory.getLogger(LibraryResource.class);
        
    @Inject
    private LibraryService libraryService;
    
    @Inject
    private LibraryMapper libraryMapper;
    
    /**
     * POST  /libraries : Create a new library.
     *
     * @param libraryDTO the libraryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new libraryDTO, or with status 400 (Bad Request) if the library has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/libraries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LibraryDTO> createLibrary(@Valid @RequestBody LibraryDTO libraryDTO) throws URISyntaxException {
        log.debug("REST request to save Library : {}", libraryDTO);
        if (libraryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("library", "idexists", "A new library cannot already have an ID")).body(null);
        }
        LibraryDTO result = libraryService.save(libraryDTO);
        return ResponseEntity.created(new URI("/api/libraries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("library", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /libraries : Updates an existing library.
     *
     * @param libraryDTO the libraryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated libraryDTO,
     * or with status 400 (Bad Request) if the libraryDTO is not valid,
     * or with status 500 (Internal Server Error) if the libraryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/libraries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LibraryDTO> updateLibrary(@Valid @RequestBody LibraryDTO libraryDTO) throws URISyntaxException {
        log.debug("REST request to update Library : {}", libraryDTO);
        if (libraryDTO.getId() == null) {
            return createLibrary(libraryDTO);
        }
        LibraryDTO result = libraryService.save(libraryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("library", libraryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /libraries : get all the libraries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of libraries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/libraries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<LibraryDTO>> getAllLibraries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Libraries");
        Page<Library> page = libraryService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/libraries");
        return new ResponseEntity<>(libraryMapper.librariesToLibraryDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /libraries/:id : get the "id" library.
     *
     * @param id the id of the libraryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the libraryDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/libraries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LibraryDTO> getLibrary(@PathVariable Long id) {
        log.debug("REST request to get Library : {}", id);
        LibraryDTO libraryDTO = libraryService.findOne(id);
        return Optional.ofNullable(libraryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /libraries/:id : delete the "id" library.
     *
     * @param id the id of the libraryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/libraries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        log.debug("REST request to delete Library : {}", id);
        libraryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("library", id.toString())).build();
    }

    /**
     * SEARCH  /_search/libraries?query=:query : search for the library corresponding
     * to the query.
     *
     * @param query the query of the library search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/libraries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<LibraryDTO>> searchLibraries(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Libraries for query {}", query);
        Page<Library> page = libraryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/libraries");
        return new ResponseEntity<>(libraryMapper.librariesToLibraryDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
