package ru.statistics.library.web.rest;

import ru.statistics.library.LibrarystatisticsApp;
import ru.statistics.library.domain.Subscription;
import ru.statistics.library.repository.SubscriptionRepository;
import ru.statistics.library.service.SubscriptionService;
import ru.statistics.library.repository.search.SubscriptionSearchRepository;
import ru.statistics.library.web.rest.dto.SubscriptionDTO;
import ru.statistics.library.web.rest.mapper.SubscriptionMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SubscriptionResource REST controller.
 *
 * @see SubscriptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibrarystatisticsApp.class)
@WebAppConfiguration
@IntegrationTest
public class SubscriptionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_FROM_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FROM_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FROM_DATE_STR = dateTimeFormatter.format(DEFAULT_FROM_DATE);

    private static final ZonedDateTime DEFAULT_TO_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TO_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TO_DATE_STR = dateTimeFormatter.format(DEFAULT_TO_DATE);

    @Inject
    private SubscriptionRepository subscriptionRepository;

    @Inject
    private SubscriptionMapper subscriptionMapper;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private SubscriptionSearchRepository subscriptionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSubscriptionMockMvc;

    private Subscription subscription;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SubscriptionResource subscriptionResource = new SubscriptionResource();
        ReflectionTestUtils.setField(subscriptionResource, "subscriptionService", subscriptionService);
        ReflectionTestUtils.setField(subscriptionResource, "subscriptionMapper", subscriptionMapper);
        this.restSubscriptionMockMvc = MockMvcBuilders.standaloneSetup(subscriptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        subscriptionSearchRepository.deleteAll();
        subscription = new Subscription();
        subscription.setFromDate(DEFAULT_FROM_DATE);
        subscription.setToDate(DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    public void createSubscription() throws Exception {
        int databaseSizeBeforeCreate = subscriptionRepository.findAll().size();

        // Create the Subscription
        SubscriptionDTO subscriptionDTO = subscriptionMapper.subscriptionToSubscriptionDTO(subscription);

        restSubscriptionMockMvc.perform(post("/api/subscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subscriptionDTO)))
                .andExpect(status().isCreated());

        // Validate the Subscription in the database
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeCreate + 1);
        Subscription testSubscription = subscriptions.get(subscriptions.size() - 1);
        assertThat(testSubscription.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testSubscription.getToDate()).isEqualTo(DEFAULT_TO_DATE);

        // Validate the Subscription in ElasticSearch
        Subscription subscriptionEs = subscriptionSearchRepository.findOne(testSubscription.getId());
        assertThat(subscriptionEs).isEqualToComparingFieldByField(testSubscription);
    }

    @Test
    @Transactional
    public void getAllSubscriptions() throws Exception {
        // Initialize the database
        subscriptionRepository.saveAndFlush(subscription);

        // Get all the subscriptions
        restSubscriptionMockMvc.perform(get("/api/subscriptions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(subscription.getId().intValue())))
                .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE_STR)))
                .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE_STR)));
    }

    @Test
    @Transactional
    public void getSubscription() throws Exception {
        // Initialize the database
        subscriptionRepository.saveAndFlush(subscription);

        // Get the subscription
        restSubscriptionMockMvc.perform(get("/api/subscriptions/{id}", subscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(subscription.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE_STR))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSubscription() throws Exception {
        // Get the subscription
        restSubscriptionMockMvc.perform(get("/api/subscriptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubscription() throws Exception {
        // Initialize the database
        subscriptionRepository.saveAndFlush(subscription);
        subscriptionSearchRepository.save(subscription);
        int databaseSizeBeforeUpdate = subscriptionRepository.findAll().size();

        // Update the subscription
        Subscription updatedSubscription = new Subscription();
        updatedSubscription.setId(subscription.getId());
        updatedSubscription.setFromDate(UPDATED_FROM_DATE);
        updatedSubscription.setToDate(UPDATED_TO_DATE);
        SubscriptionDTO subscriptionDTO = subscriptionMapper.subscriptionToSubscriptionDTO(updatedSubscription);

        restSubscriptionMockMvc.perform(put("/api/subscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subscriptionDTO)))
                .andExpect(status().isOk());

        // Validate the Subscription in the database
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeUpdate);
        Subscription testSubscription = subscriptions.get(subscriptions.size() - 1);
        assertThat(testSubscription.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testSubscription.getToDate()).isEqualTo(UPDATED_TO_DATE);

        // Validate the Subscription in ElasticSearch
        Subscription subscriptionEs = subscriptionSearchRepository.findOne(testSubscription.getId());
        assertThat(subscriptionEs).isEqualToComparingFieldByField(testSubscription);
    }

    @Test
    @Transactional
    public void deleteSubscription() throws Exception {
        // Initialize the database
        subscriptionRepository.saveAndFlush(subscription);
        subscriptionSearchRepository.save(subscription);
        int databaseSizeBeforeDelete = subscriptionRepository.findAll().size();

        // Get the subscription
        restSubscriptionMockMvc.perform(delete("/api/subscriptions/{id}", subscription.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean subscriptionExistsInEs = subscriptionSearchRepository.exists(subscription.getId());
        assertThat(subscriptionExistsInEs).isFalse();

        // Validate the database is empty
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        assertThat(subscriptions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSubscription() throws Exception {
        // Initialize the database
        subscriptionRepository.saveAndFlush(subscription);
        subscriptionSearchRepository.save(subscription);

        // Search the subscription
        restSubscriptionMockMvc.perform(get("/api/_search/subscriptions?query=id:" + subscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE_STR)))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE_STR)));
    }
}
