package ru.statistics.library.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.statistics.library.domain.Subscription;
import ru.statistics.library.service.SubscriptionService;
import ru.statistics.library.web.rest.util.HeaderUtil;
import ru.statistics.library.web.rest.util.PaginationUtil;
import ru.statistics.library.web.rest.dto.SubscriptionDTO;
import ru.statistics.library.web.rest.mapper.SubscriptionMapper;
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
 * REST controller for managing Subscription.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionResource.class);
        
    @Inject
    private SubscriptionService subscriptionService;
    
    @Inject
    private SubscriptionMapper subscriptionMapper;
    
    /**
     * POST  /subscriptions : Create a new subscription.
     *
     * @param subscriptionDTO the subscriptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subscriptionDTO, or with status 400 (Bad Request) if the subscription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subscriptions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save Subscription : {}", subscriptionDTO);
        if (subscriptionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subscription", "idexists", "A new subscription cannot already have an ID")).body(null);
        }
        SubscriptionDTO result = subscriptionService.save(subscriptionDTO);
        return ResponseEntity.created(new URI("/api/subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("subscription", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subscriptions : Updates an existing subscription.
     *
     * @param subscriptionDTO the subscriptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subscriptionDTO,
     * or with status 400 (Bad Request) if the subscriptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the subscriptionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subscriptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubscriptionDTO> updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) throws URISyntaxException {
        log.debug("REST request to update Subscription : {}", subscriptionDTO);
        if (subscriptionDTO.getId() == null) {
            return createSubscription(subscriptionDTO);
        }
        SubscriptionDTO result = subscriptionService.save(subscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("subscription", subscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subscriptions : get all the subscriptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subscriptions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/subscriptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Subscriptions");
        Page<Subscription> page = subscriptionService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subscriptions");
        return new ResponseEntity<>(subscriptionMapper.subscriptionsToSubscriptionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /subscriptions/:id : get the "id" subscription.
     *
     * @param id the id of the subscriptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subscriptionDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/subscriptions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SubscriptionDTO> getSubscription(@PathVariable Long id) {
        log.debug("REST request to get Subscription : {}", id);
        SubscriptionDTO subscriptionDTO = subscriptionService.findOne(id);
        return Optional.ofNullable(subscriptionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /subscriptions/:id : delete the "id" subscription.
     *
     * @param id the id of the subscriptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/subscriptions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        log.debug("REST request to delete Subscription : {}", id);
        subscriptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subscription", id.toString())).build();
    }

    /**
     * SEARCH  /_search/subscriptions?query=:query : search for the subscription corresponding
     * to the query.
     *
     * @param query the query of the subscription search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/subscriptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SubscriptionDTO>> searchSubscriptions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Subscriptions for query {}", query);
        Page<Subscription> page = subscriptionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/subscriptions");
        return new ResponseEntity<>(subscriptionMapper.subscriptionsToSubscriptionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
