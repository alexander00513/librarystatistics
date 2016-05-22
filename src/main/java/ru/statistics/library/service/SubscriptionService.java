package ru.statistics.library.service;

import ru.statistics.library.domain.Subscription;
import ru.statistics.library.web.rest.dto.SubscriptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Subscription.
 */
public interface SubscriptionService {

    /**
     * Save a subscription.
     * 
     * @param subscriptionDTO the entity to save
     * @return the persisted entity
     */
    SubscriptionDTO save(SubscriptionDTO subscriptionDTO);

    /**
     *  Get all the subscriptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Subscription> findAll(Pageable pageable);

    /**
     *  Get the "id" subscription.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    SubscriptionDTO findOne(Long id);

    /**
     *  Delete the "id" subscription.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the subscription corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Subscription> search(String query, Pageable pageable);
}
