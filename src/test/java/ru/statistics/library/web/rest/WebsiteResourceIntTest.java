package ru.statistics.library.web.rest;

import ru.statistics.library.LibrarystatisticsApp;
import ru.statistics.library.domain.Website;
import ru.statistics.library.repository.WebsiteRepository;
import ru.statistics.library.service.WebsiteService;
import ru.statistics.library.repository.search.WebsiteSearchRepository;
import ru.statistics.library.web.rest.dto.WebsiteDTO;
import ru.statistics.library.web.rest.mapper.WebsiteMapper;

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

import ru.statistics.library.domain.enumeration.WebsiteType;

/**
 * Test class for the WebsiteResource REST controller.
 *
 * @see WebsiteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibrarystatisticsApp.class)
@WebAppConfiguration
@IntegrationTest
public class WebsiteResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    private static final Long DEFAULT_VISITS = 1L;
    private static final Long UPDATED_VISITS = 2L;

    private static final WebsiteType DEFAULT_TYPE = WebsiteType.WEBSITE;
    private static final WebsiteType UPDATED_TYPE = WebsiteType.BLOG;

    @Inject
    private WebsiteRepository websiteRepository;

    @Inject
    private WebsiteMapper websiteMapper;

    @Inject
    private WebsiteService websiteService;

    @Inject
    private WebsiteSearchRepository websiteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWebsiteMockMvc;

    private Website website;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WebsiteResource websiteResource = new WebsiteResource();
        ReflectionTestUtils.setField(websiteResource, "websiteService", websiteService);
        ReflectionTestUtils.setField(websiteResource, "websiteMapper", websiteMapper);
        this.restWebsiteMockMvc = MockMvcBuilders.standaloneSetup(websiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        websiteSearchRepository.deleteAll();
        website = new Website();
        website.setUrl(DEFAULT_URL);
        website.setVisits(DEFAULT_VISITS);
        website.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createWebsite() throws Exception {
        int databaseSizeBeforeCreate = websiteRepository.findAll().size();

        // Create the Website
        WebsiteDTO websiteDTO = websiteMapper.websiteToWebsiteDTO(website);

        restWebsiteMockMvc.perform(post("/api/websites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(websiteDTO)))
                .andExpect(status().isCreated());

        // Validate the Website in the database
        List<Website> websites = websiteRepository.findAll();
        assertThat(websites).hasSize(databaseSizeBeforeCreate + 1);
        Website testWebsite = websites.get(websites.size() - 1);
        assertThat(testWebsite.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testWebsite.getVisits()).isEqualTo(DEFAULT_VISITS);
        assertThat(testWebsite.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Website in ElasticSearch
        Website websiteEs = websiteSearchRepository.findOne(testWebsite.getId());
        assertThat(websiteEs).isEqualToComparingFieldByField(testWebsite);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = websiteRepository.findAll().size();
        // set the field null
        website.setUrl(null);

        // Create the Website, which fails.
        WebsiteDTO websiteDTO = websiteMapper.websiteToWebsiteDTO(website);

        restWebsiteMockMvc.perform(post("/api/websites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(websiteDTO)))
                .andExpect(status().isBadRequest());

        List<Website> websites = websiteRepository.findAll();
        assertThat(websites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = websiteRepository.findAll().size();
        // set the field null
        website.setType(null);

        // Create the Website, which fails.
        WebsiteDTO websiteDTO = websiteMapper.websiteToWebsiteDTO(website);

        restWebsiteMockMvc.perform(post("/api/websites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(websiteDTO)))
                .andExpect(status().isBadRequest());

        List<Website> websites = websiteRepository.findAll();
        assertThat(websites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWebsites() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);

        // Get all the websites
        restWebsiteMockMvc.perform(get("/api/websites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(website.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].visits").value(hasItem(DEFAULT_VISITS.intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getWebsite() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);

        // Get the website
        restWebsiteMockMvc.perform(get("/api/websites/{id}", website.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(website.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.visits").value(DEFAULT_VISITS.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWebsite() throws Exception {
        // Get the website
        restWebsiteMockMvc.perform(get("/api/websites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWebsite() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);
        websiteSearchRepository.save(website);
        int databaseSizeBeforeUpdate = websiteRepository.findAll().size();

        // Update the website
        Website updatedWebsite = new Website();
        updatedWebsite.setId(website.getId());
        updatedWebsite.setUrl(UPDATED_URL);
        updatedWebsite.setVisits(UPDATED_VISITS);
        updatedWebsite.setType(UPDATED_TYPE);
        WebsiteDTO websiteDTO = websiteMapper.websiteToWebsiteDTO(updatedWebsite);

        restWebsiteMockMvc.perform(put("/api/websites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(websiteDTO)))
                .andExpect(status().isOk());

        // Validate the Website in the database
        List<Website> websites = websiteRepository.findAll();
        assertThat(websites).hasSize(databaseSizeBeforeUpdate);
        Website testWebsite = websites.get(websites.size() - 1);
        assertThat(testWebsite.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testWebsite.getVisits()).isEqualTo(UPDATED_VISITS);
        assertThat(testWebsite.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Website in ElasticSearch
        Website websiteEs = websiteSearchRepository.findOne(testWebsite.getId());
        assertThat(websiteEs).isEqualToComparingFieldByField(testWebsite);
    }

    @Test
    @Transactional
    public void deleteWebsite() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);
        websiteSearchRepository.save(website);
        int databaseSizeBeforeDelete = websiteRepository.findAll().size();

        // Get the website
        restWebsiteMockMvc.perform(delete("/api/websites/{id}", website.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean websiteExistsInEs = websiteSearchRepository.exists(website.getId());
        assertThat(websiteExistsInEs).isFalse();

        // Validate the database is empty
        List<Website> websites = websiteRepository.findAll();
        assertThat(websites).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWebsite() throws Exception {
        // Initialize the database
        websiteRepository.saveAndFlush(website);
        websiteSearchRepository.save(website);

        // Search the website
        restWebsiteMockMvc.perform(get("/api/_search/websites?query=id:" + website.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(website.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].visits").value(hasItem(DEFAULT_VISITS.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
}
