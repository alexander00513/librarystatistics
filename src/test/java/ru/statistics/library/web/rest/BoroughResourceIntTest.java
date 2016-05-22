package ru.statistics.library.web.rest;

import ru.statistics.library.LibrarystatisticsApp;
import ru.statistics.library.domain.Borough;
import ru.statistics.library.repository.BoroughRepository;
import ru.statistics.library.service.BoroughService;
import ru.statistics.library.repository.search.BoroughSearchRepository;
import ru.statistics.library.web.rest.dto.BoroughDTO;
import ru.statistics.library.web.rest.mapper.BoroughMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BoroughResource REST controller.
 *
 * @see BoroughResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibrarystatisticsApp.class)
@WebAppConfiguration
@IntegrationTest
public class BoroughResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private BoroughRepository boroughRepository;

    @Inject
    private BoroughMapper boroughMapper;

    @Inject
    private BoroughService boroughService;

    @Inject
    private BoroughSearchRepository boroughSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBoroughMockMvc;

    private Borough borough;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BoroughResource boroughResource = new BoroughResource();
        ReflectionTestUtils.setField(boroughResource, "boroughService", boroughService);
        ReflectionTestUtils.setField(boroughResource, "boroughMapper", boroughMapper);
        this.restBoroughMockMvc = MockMvcBuilders.standaloneSetup(boroughResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        boroughSearchRepository.deleteAll();
        borough = new Borough();
        borough.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBorough() throws Exception {
        int databaseSizeBeforeCreate = boroughRepository.findAll().size();

        // Create the Borough
        BoroughDTO boroughDTO = boroughMapper.boroughToBoroughDTO(borough);

        restBoroughMockMvc.perform(post("/api/boroughs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boroughDTO)))
                .andExpect(status().isCreated());

        // Validate the Borough in the database
        List<Borough> boroughs = boroughRepository.findAll();
        assertThat(boroughs).hasSize(databaseSizeBeforeCreate + 1);
        Borough testBorough = boroughs.get(boroughs.size() - 1);
        assertThat(testBorough.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Borough in ElasticSearch
        Borough boroughEs = boroughSearchRepository.findOne(testBorough.getId());
        assertThat(boroughEs).isEqualToComparingFieldByField(testBorough);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = boroughRepository.findAll().size();
        // set the field null
        borough.setName(null);

        // Create the Borough, which fails.
        BoroughDTO boroughDTO = boroughMapper.boroughToBoroughDTO(borough);

        restBoroughMockMvc.perform(post("/api/boroughs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boroughDTO)))
                .andExpect(status().isBadRequest());

        List<Borough> boroughs = boroughRepository.findAll();
        assertThat(boroughs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBoroughs() throws Exception {
        // Initialize the database
        boroughRepository.saveAndFlush(borough);

        // Get all the boroughs
        restBoroughMockMvc.perform(get("/api/boroughs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(borough.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBorough() throws Exception {
        // Initialize the database
        boroughRepository.saveAndFlush(borough);

        // Get the borough
        restBoroughMockMvc.perform(get("/api/boroughs/{id}", borough.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(borough.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBorough() throws Exception {
        // Get the borough
        restBoroughMockMvc.perform(get("/api/boroughs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBorough() throws Exception {
        // Initialize the database
        boroughRepository.saveAndFlush(borough);
        boroughSearchRepository.save(borough);
        int databaseSizeBeforeUpdate = boroughRepository.findAll().size();

        // Update the borough
        Borough updatedBorough = new Borough();
        updatedBorough.setId(borough.getId());
        updatedBorough.setName(UPDATED_NAME);
        BoroughDTO boroughDTO = boroughMapper.boroughToBoroughDTO(updatedBorough);

        restBoroughMockMvc.perform(put("/api/boroughs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(boroughDTO)))
                .andExpect(status().isOk());

        // Validate the Borough in the database
        List<Borough> boroughs = boroughRepository.findAll();
        assertThat(boroughs).hasSize(databaseSizeBeforeUpdate);
        Borough testBorough = boroughs.get(boroughs.size() - 1);
        assertThat(testBorough.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Borough in ElasticSearch
        Borough boroughEs = boroughSearchRepository.findOne(testBorough.getId());
        assertThat(boroughEs).isEqualToComparingFieldByField(testBorough);
    }

    @Test
    @Transactional
    public void deleteBorough() throws Exception {
        // Initialize the database
        boroughRepository.saveAndFlush(borough);
        boroughSearchRepository.save(borough);
        int databaseSizeBeforeDelete = boroughRepository.findAll().size();

        // Get the borough
        restBoroughMockMvc.perform(delete("/api/boroughs/{id}", borough.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean boroughExistsInEs = boroughSearchRepository.exists(borough.getId());
        assertThat(boroughExistsInEs).isFalse();

        // Validate the database is empty
        List<Borough> boroughs = boroughRepository.findAll();
        assertThat(boroughs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBorough() throws Exception {
        // Initialize the database
        boroughRepository.saveAndFlush(borough);
        boroughSearchRepository.save(borough);

        // Search the borough
        restBoroughMockMvc.perform(get("/api/_search/boroughs?query=id:" + borough.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(borough.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
