package ru.statistics.library.service.impl;

import ru.statistics.library.service.SubscriptionService;
import ru.statistics.library.domain.Subscription;
import ru.statistics.library.repository.SubscriptionRepository;
import ru.statistics.library.repository.search.SubscriptionSearchRepository;
import ru.statistics.library.web.rest.dto.SubscriptionDTO;
import ru.statistics.library.web.rest.mapper.SubscriptionMapper;
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
 * Service Implementation for managing Subscription.
 */
@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService{

    private final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);
    
    @Inject
    private SubscriptionRepository subscriptionRepository;
    
    @Inject
    private SubscriptionMapper subscriptionMapper;
    
    @Inject
    private SubscriptionSearchRepository subscriptionSearchRepository;
    
    /**
     * Save a subscription.
     * 
     * @param subscriptionDTO the entity to save
     * @return the persisted entity
     */
    public SubscriptionDTO save(SubscriptionDTO subscriptionDTO) {
        log.debug("Request to save Subscription : {}", subscriptionDTO);
        Subscription subscription = subscriptionMapper.subscriptionDTOToSubscription(subscriptionDTO);
        subscription = subscriptionRepository.save(subscription);
        SubscriptionDTO result = subscriptionMapper.subscriptionToSubscriptionDTO(subscription);
        subscriptionSearchRepository.save(subscription);
        return result;
    }

    /**
     *  Get all the subscriptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Subscription> findAll(Pageable pageable) {
        log.debug("Request to get all Subscriptions");
        Page<Subscription> result = subscriptionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one subscription by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SubscriptionDTO findOne(Long id) {
        log.debug("Request to get Subscription : {}", id);
        Subscription subscription = subscriptionRepository.findOne(id);
        SubscriptionDTO subscriptionDTO = subscriptionMapper.subscriptionToSubscriptionDTO(subscription);
        return subscriptionDTO;
    }

    /**
     *  Delete the  subscription by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Subscription : {}", id);
        subscriptionRepository.delete(id);
        subscriptionSearchRepository.delete(id);
    }

    /**
     * Search for the subscription corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Subscription> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Subscriptions for query {}", query);
        return subscriptionSearchRepository.search(queryStringQuery(query), pageable);
    }
}
