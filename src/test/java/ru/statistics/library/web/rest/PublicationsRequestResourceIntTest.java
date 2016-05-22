package ru.statistics.library.web.rest;

import ru.statistics.library.LibrarystatisticsApp;
import ru.statistics.library.domain.PublicationsRequest;
import ru.statistics.library.repository.PublicationsRequestRepository;
import ru.statistics.library.service.PublicationsRequestService;
import ru.statistics.library.repository.search.PublicationsRequestSearchRepository;
import ru.statistics.library.web.rest.dto.PublicationsRequestDTO;
import ru.statistics.library.web.rest.mapper.PublicationsRequestMapper;

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
 * Test class for the PublicationsRequestResource REST controller.
 *
 * @see PublicationsRequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibrarystatisticsApp.class)
@WebAppConfiguration
@IntegrationTest
public class PublicationsRequestResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_FROM_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FROM_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FROM_DATE_STR = dateTimeFormatter.format(DEFAULT_FROM_DATE);

    private static final ZonedDateTime DEFAULT_TO_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TO_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TO_DATE_STR = dateTimeFormatter.format(DEFAULT_TO_DATE);

    @Inject
    private PublicationsRequestRepository publicationsRequestRepository;

    @Inject
    private PublicationsRequestMapper publicationsRequestMapper;

    @Inject
    private PublicationsRequestService publicationsRequestService;

    @Inject
    private PublicationsRequestSearchRepository publicationsRequestSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPublicationsRequestMockMvc;

    private PublicationsRequest publicationsRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PublicationsRequestResource publicationsRequestResource = new PublicationsRequestResource();
        ReflectionTestUtils.setField(publicationsRequestResource, "publicationsRequestService", publicationsRequestService);
        ReflectionTestUtils.setField(publicationsRequestResource, "publicationsRequestMapper", publicationsRequestMapper);
        this.restPublicationsRequestMockMvc = MockMvcBuilders.standaloneSetup(publicationsRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        publicationsRequestSearchRepository.deleteAll();
        publicationsRequest = new PublicationsRequest();
        publicationsRequest.setFromDate(DEFAULT_FROM_DATE);
        publicationsRequest.setToDate(DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    public void createPublicationsRequest() throws Exception {
        int databaseSizeBeforeCreate = publicationsRequestRepository.findAll().size();

        // Create the PublicationsRequest
        PublicationsRequestDTO publicationsRequestDTO = publicationsRequestMapper.publicationsRequestToPublicationsRequestDTO(publicationsRequest);

        restPublicationsRequestMockMvc.perform(post("/api/publications-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publicationsRequestDTO)))
                .andExpect(status().isCreated());

        // Validate the PublicationsRequest in the database
        List<PublicationsRequest> publicationsRequests = publicationsRequestRepository.findAll();
        assertThat(publicationsRequests).hasSize(databaseSizeBeforeCreate + 1);
        PublicationsRequest testPublicationsRequest = publicationsRequests.get(publicationsRequests.size() - 1);
        assertThat(testPublicationsRequest.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testPublicationsRequest.getToDate()).isEqualTo(DEFAULT_TO_DATE);

        // Validate the PublicationsRequest in ElasticSearch
        PublicationsRequest publicationsRequestEs = publicationsRequestSearchRepository.findOne(testPublicationsRequest.getId());
        assertThat(publicationsRequestEs).isEqualToComparingFieldByField(testPublicationsRequest);
    }

    @Test
    @Transactional
    public void getAllPublicationsRequests() throws Exception {
        // Initialize the database
        publicationsRequestRepository.saveAndFlush(publicationsRequest);

        // Get all the publicationsRequests
        restPublicationsRequestMockMvc.perform(get("/api/publications-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(publicationsRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE_STR)))
                .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE_STR)));
    }

    @Test
    @Transactional
    public void getPublicationsRequest() throws Exception {
        // Initialize the database
        publicationsRequestRepository.saveAndFlush(publicationsRequest);

        // Get the publicationsRequest
        restPublicationsRequestMockMvc.perform(get("/api/publications-requests/{id}", publicationsRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(publicationsRequest.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE_STR))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPublicationsRequest() throws Exception {
        // Get the publicationsRequest
        restPublicationsRequestMockMvc.perform(get("/api/publications-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublicationsRequest() throws Exception {
        // Initialize the database
        publicationsRequestRepository.saveAndFlush(publicationsRequest);
        publicationsRequestSearchRepository.save(publicationsRequest);
        int databaseSizeBeforeUpdate = publicationsRequestRepository.findAll().size();

        // Update the publicationsRequest
        PublicationsRequest updatedPublicationsRequest = new PublicationsRequest();
        updatedPublicationsRequest.setId(publicationsRequest.getId());
        updatedPublicationsRequest.setFromDate(UPDATED_FROM_DATE);
        updatedPublicationsRequest.setToDate(UPDATED_TO_DATE);
        PublicationsRequestDTO publicationsRequestDTO = publicationsRequestMapper.publicationsRequestToPublicationsRequestDTO(updatedPublicationsRequest);

        restPublicationsRequestMockMvc.perform(put("/api/publications-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publicationsRequestDTO)))
                .andExpect(status().isOk());

        // Validate the PublicationsRequest in the database
        List<PublicationsRequest> publicationsRequests = publicationsRequestRepository.findAll();
        assertThat(publicationsRequests).hasSize(databaseSizeBeforeUpdate);
        PublicationsRequest testPublicationsRequest = publicationsRequests.get(publicationsRequests.size() - 1);
        assertThat(testPublicationsRequest.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testPublicationsRequest.getToDate()).isEqualTo(UPDATED_TO_DATE);

        // Validate the PublicationsRequest in ElasticSearch
        PublicationsRequest publicationsRequestEs = publicationsRequestSearchRepository.findOne(testPublicationsRequest.getId());
        assertThat(publicationsRequestEs).isEqualToComparingFieldByField(testPublicationsRequest);
    }

    @Test
    @Transactional
    public void deletePublicationsRequest() throws Exception {
        // Initialize the database
        publicationsRequestRepository.saveAndFlush(publicationsRequest);
        publicationsRequestSearchRepository.save(publicationsRequest);
        int databaseSizeBeforeDelete = publicationsRequestRepository.findAll().size();

        // Get the publicationsRequest
        restPublicationsRequestMockMvc.perform(delete("/api/publications-requests/{id}", publicationsRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean publicationsRequestExistsInEs = publicationsRequestSearchRepository.exists(publicationsRequest.getId());
        assertThat(publicationsRequestExistsInEs).isFalse();

        // Validate the database is empty
        List<PublicationsRequest> publicationsRequests = publicationsRequestRepository.findAll();
        assertThat(publicationsRequests).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPublicationsRequest() throws Exception {
        // Initialize the database
        publicationsRequestRepository.saveAndFlush(publicationsRequest);
        publicationsRequestSearchRepository.save(publicationsRequest);

        // Search the publicationsRequest
        restPublicationsRequestMockMvc.perform(get("/api/_search/publications-requests?query=id:" + publicationsRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicationsRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE_STR)))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE_STR)));
    }
}
