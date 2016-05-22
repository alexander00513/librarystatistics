package ru.statistics.library.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.statistics.library.domain.Equipment;
import ru.statistics.library.service.EquipmentService;
import ru.statistics.library.web.rest.util.HeaderUtil;
import ru.statistics.library.web.rest.util.PaginationUtil;
import ru.statistics.library.web.rest.dto.EquipmentDTO;
import ru.statistics.library.web.rest.mapper.EquipmentMapper;
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
 * REST controller for managing Equipment.
 */
@RestController
@RequestMapping("/api")
public class EquipmentResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentResource.class);
        
    @Inject
    private EquipmentService equipmentService;
    
    @Inject
    private EquipmentMapper equipmentMapper;
    
    /**
     * POST  /equipment : Create a new equipment.
     *
     * @param equipmentDTO the equipmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipmentDTO, or with status 400 (Bad Request) if the equipment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/equipment",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EquipmentDTO> createEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("equipment", "idexists", "A new equipment cannot already have an ID")).body(null);
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.created(new URI("/api/equipment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("equipment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipment : Updates an existing equipment.
     *
     * @param equipmentDTO the equipmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipmentDTO,
     * or with status 400 (Bad Request) if the equipmentDTO is not valid,
     * or with status 500 (Internal Server Error) if the equipmentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/equipment",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EquipmentDTO> updateEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to update Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() == null) {
            return createEquipment(equipmentDTO);
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("equipment", equipmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipment : get all the equipment.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of equipment in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/equipment",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Equipment");
        Page<Equipment> page = equipmentService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equipment");
        return new ResponseEntity<>(equipmentMapper.equipmentToEquipmentDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /equipment/:id : get the "id" equipment.
     *
     * @param id the id of the equipmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipmentDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/equipment/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EquipmentDTO> getEquipment(@PathVariable Long id) {
        log.debug("REST request to get Equipment : {}", id);
        EquipmentDTO equipmentDTO = equipmentService.findOne(id);
        return Optional.ofNullable(equipmentDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /equipment/:id : delete the "id" equipment.
     *
     * @param id the id of the equipmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/equipment/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);
        equipmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("equipment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/equipment?query=:query : search for the equipment corresponding
     * to the query.
     *
     * @param query the query of the equipment search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/equipment",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<EquipmentDTO>> searchEquipment(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Equipment for query {}", query);
        Page<Equipment> page = equipmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/equipment");
        return new ResponseEntity<>(equipmentMapper.equipmentToEquipmentDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
