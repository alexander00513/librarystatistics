package ru.statistics.library.web.rest;

import ru.statistics.library.LibrarystatisticsApp;
import ru.statistics.library.domain.Publication;
import ru.statistics.library.repository.PublicationRepository;
import ru.statistics.library.service.PublicationService;
import ru.statistics.library.repository.search.PublicationSearchRepository;
import ru.statistics.library.web.rest.dto.PublicationDTO;
import ru.statistics.library.web.rest.mapper.PublicationMapper;

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

import ru.statistics.library.domain.enumeration.PublicationType;

/**
 * Test class for the PublicationResource REST controller.
 *
 * @see PublicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibrarystatisticsApp.class)
@WebAppConfiguration
@IntegrationTest
public class PublicationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_AUTHOR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ISBN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ISBN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final PublicationType DEFAULT_TYPE = PublicationType.PRINTED;
    private static final PublicationType UPDATED_TYPE = PublicationType.ELECTRONIC;

    @Inject
    private PublicationRepository publicationRepository;

    @Inject
    private PublicationMapper publicationMapper;

    @Inject
    private PublicationService publicationService;

    @Inject
    private PublicationSearchRepository publicationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPublicationMockMvc;

    private Publication publication;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PublicationResource publicationResource = new PublicationResource();
        ReflectionTestUtils.setField(publicationResource, "publicationService", publicationService);
        ReflectionTestUtils.setField(publicationResource, "publicationMapper", publicationMapper);
        this.restPublicationMockMvc = MockMvcBuilders.standaloneSetup(publicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        publicationSearchRepository.deleteAll();
        publication = new Publication();
        publication.setName(DEFAULT_NAME);
        publication.setAuthor(DEFAULT_AUTHOR);
        publication.setIsbn(DEFAULT_ISBN);
        publication.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createPublication() throws Exception {
        int databaseSizeBeforeCreate = publicationRepository.findAll().size();

        // Create the Publication
        PublicationDTO publicationDTO = publicationMapper.publicationToPublicationDTO(publication);

        restPublicationMockMvc.perform(post("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
                .andExpect(status().isCreated());

        // Validate the Publication in the database
        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeCreate + 1);
        Publication testPublication = publications.get(publications.size() - 1);
        assertThat(testPublication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPublication.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testPublication.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testPublication.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Publication in ElasticSearch
        Publication publicationEs = publicationSearchRepository.findOne(testPublication.getId());
        assertThat(publicationEs).isEqualToComparingFieldByField(testPublication);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicationRepository.findAll().size();
        // set the field null
        publication.setName(null);

        // Create the Publication, which fails.
        PublicationDTO publicationDTO = publicationMapper.publicationToPublicationDTO(publication);

        restPublicationMockMvc.perform(post("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
                .andExpect(status().isBadRequest());

        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicationRepository.findAll().size();
        // set the field null
        publication.setAuthor(null);

        // Create the Publication, which fails.
        PublicationDTO publicationDTO = publicationMapper.publicationToPublicationDTO(publication);

        restPublicationMockMvc.perform(post("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
                .andExpect(status().isBadRequest());

        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicationRepository.findAll().size();
        // set the field null
        publication.setType(null);

        // Create the Publication, which fails.
        PublicationDTO publicationDTO = publicationMapper.publicationToPublicationDTO(publication);

        restPublicationMockMvc.perform(post("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
                .andExpect(status().isBadRequest());

        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPublications() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publications
        restPublicationMockMvc.perform(get("/api/publications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(publication.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get the publication
        restPublicationMockMvc.perform(get("/api/publications/{id}", publication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(publication.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPublication() throws Exception {
        // Get the publication
        restPublicationMockMvc.perform(get("/api/publications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);
        publicationSearchRepository.save(publication);
        int databaseSizeBeforeUpdate = publicationRepository.findAll().size();

        // Update the publication
        Publication updatedPublication = new Publication();
        updatedPublication.setId(publication.getId());
        updatedPublication.setName(UPDATED_NAME);
        updatedPublication.setAuthor(UPDATED_AUTHOR);
        updatedPublication.setIsbn(UPDATED_ISBN);
        updatedPublication.setType(UPDATED_TYPE);
        PublicationDTO publicationDTO = publicationMapper.publicationToPublicationDTO(updatedPublication);

        restPublicationMockMvc.perform(put("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
                .andExpect(status().isOk());

        // Validate the Publication in the database
        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeUpdate);
        Publication testPublication = publications.get(publications.size() - 1);
        assertThat(testPublication.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPublication.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testPublication.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testPublication.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Publication in ElasticSearch
        Publication publicationEs = publicationSearchRepository.findOne(testPublication.getId());
        assertThat(publicationEs).isEqualToComparingFieldByField(testPublication);
    }

    @Test
    @Transactional
    public void deletePublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);
        publicationSearchRepository.save(publication);
        int databaseSizeBeforeDelete = publicationRepository.findAll().size();

        // Get the publication
        restPublicationMockMvc.perform(delete("/api/publications/{id}", publication.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean publicationExistsInEs = publicationSearchRepository.exists(publication.getId());
        assertThat(publicationExistsInEs).isFalse();

        // Validate the database is empty
        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);
        publicationSearchRepository.save(publication);

        // Search the publication
        restPublicationMockMvc.perform(get("/api/_search/publications?query=id:" + publication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publication.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
}
