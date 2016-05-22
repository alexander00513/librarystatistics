package ru.statistics.library.web.rest;

import ru.statistics.library.LibrarystatisticsApp;
import ru.statistics.library.domain.Library;
import ru.statistics.library.repository.LibraryRepository;
import ru.statistics.library.service.LibraryService;
import ru.statistics.library.repository.search.LibrarySearchRepository;
import ru.statistics.library.web.rest.dto.LibraryDTO;
import ru.statistics.library.web.rest.mapper.LibraryMapper;

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

import ru.statistics.library.domain.enumeration.LibraryType;

/**
 * Test class for the LibraryResource REST controller.
 *
 * @see LibraryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibrarystatisticsApp.class)
@WebAppConfiguration
@IntegrationTest
public class LibraryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LibraryType DEFAULT_TYPE = LibraryType.REPUBLICAN;
    private static final LibraryType UPDATED_TYPE = LibraryType.PUBLIC;

    private static final Boolean DEFAULT_INTERNET_ACCESS = false;
    private static final Boolean UPDATED_INTERNET_ACCESS = true;

    @Inject
    private LibraryRepository libraryRepository;

    @Inject
    private LibraryMapper libraryMapper;

    @Inject
    private LibraryService libraryService;

    @Inject
    private LibrarySearchRepository librarySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLibraryMockMvc;

    private Library library;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LibraryResource libraryResource = new LibraryResource();
        ReflectionTestUtils.setField(libraryResource, "libraryService", libraryService);
        ReflectionTestUtils.setField(libraryResource, "libraryMapper", libraryMapper);
        this.restLibraryMockMvc = MockMvcBuilders.standaloneSetup(libraryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        librarySearchRepository.deleteAll();
        library = new Library();
        library.setName(DEFAULT_NAME);
        library.setDescription(DEFAULT_DESCRIPTION);
        library.setType(DEFAULT_TYPE);
        library.setInternetAccess(DEFAULT_INTERNET_ACCESS);
    }

    @Test
    @Transactional
    public void createLibrary() throws Exception {
        int databaseSizeBeforeCreate = libraryRepository.findAll().size();

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.libraryToLibraryDTO(library);

        restLibraryMockMvc.perform(post("/api/libraries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
                .andExpect(status().isCreated());

        // Validate the Library in the database
        List<Library> libraries = libraryRepository.findAll();
        assertThat(libraries).hasSize(databaseSizeBeforeCreate + 1);
        Library testLibrary = libraries.get(libraries.size() - 1);
        assertThat(testLibrary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLibrary.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLibrary.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLibrary.isInternetAccess()).isEqualTo(DEFAULT_INTERNET_ACCESS);

        // Validate the Library in ElasticSearch
        Library libraryEs = librarySearchRepository.findOne(testLibrary.getId());
        assertThat(libraryEs).isEqualToComparingFieldByField(testLibrary);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = libraryRepository.findAll().size();
        // set the field null
        library.setName(null);

        // Create the Library, which fails.
        LibraryDTO libraryDTO = libraryMapper.libraryToLibraryDTO(library);

        restLibraryMockMvc.perform(post("/api/libraries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
                .andExpect(status().isBadRequest());

        List<Library> libraries = libraryRepository.findAll();
        assertThat(libraries).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = libraryRepository.findAll().size();
        // set the field null
        library.setDescription(null);

        // Create the Library, which fails.
        LibraryDTO libraryDTO = libraryMapper.libraryToLibraryDTO(library);

        restLibraryMockMvc.perform(post("/api/libraries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
                .andExpect(status().isBadRequest());

        List<Library> libraries = libraryRepository.findAll();
        assertThat(libraries).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = libraryRepository.findAll().size();
        // set the field null
        library.setType(null);

        // Create the Library, which fails.
        LibraryDTO libraryDTO = libraryMapper.libraryToLibraryDTO(library);

        restLibraryMockMvc.perform(post("/api/libraries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
                .andExpect(status().isBadRequest());

        List<Library> libraries = libraryRepository.findAll();
        assertThat(libraries).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLibraries() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        // Get all the libraries
        restLibraryMockMvc.perform(get("/api/libraries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(library.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].internetAccess").value(hasItem(DEFAULT_INTERNET_ACCESS.booleanValue())));
    }

    @Test
    @Transactional
    public void getLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        // Get the library
        restLibraryMockMvc.perform(get("/api/libraries/{id}", library.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(library.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.internetAccess").value(DEFAULT_INTERNET_ACCESS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLibrary() throws Exception {
        // Get the library
        restLibraryMockMvc.perform(get("/api/libraries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);
        librarySearchRepository.save(library);
        int databaseSizeBeforeUpdate = libraryRepository.findAll().size();

        // Update the library
        Library updatedLibrary = new Library();
        updatedLibrary.setId(library.getId());
        updatedLibrary.setName(UPDATED_NAME);
        updatedLibrary.setDescription(UPDATED_DESCRIPTION);
        updatedLibrary.setType(UPDATED_TYPE);
        updatedLibrary.setInternetAccess(UPDATED_INTERNET_ACCESS);
        LibraryDTO libraryDTO = libraryMapper.libraryToLibraryDTO(updatedLibrary);

        restLibraryMockMvc.perform(put("/api/libraries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(libraryDTO)))
                .andExpect(status().isOk());

        // Validate the Library in the database
        List<Library> libraries = libraryRepository.findAll();
        assertThat(libraries).hasSize(databaseSizeBeforeUpdate);
        Library testLibrary = libraries.get(libraries.size() - 1);
        assertThat(testLibrary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLibrary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLibrary.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLibrary.isInternetAccess()).isEqualTo(UPDATED_INTERNET_ACCESS);

        // Validate the Library in ElasticSearch
        Library libraryEs = librarySearchRepository.findOne(testLibrary.getId());
        assertThat(libraryEs).isEqualToComparingFieldByField(testLibrary);
    }

    @Test
    @Transactional
    public void deleteLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);
        librarySearchRepository.save(library);
        int databaseSizeBeforeDelete = libraryRepository.findAll().size();

        // Get the library
        restLibraryMockMvc.perform(delete("/api/libraries/{id}", library.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean libraryExistsInEs = librarySearchRepository.exists(library.getId());
        assertThat(libraryExistsInEs).isFalse();

        // Validate the database is empty
        List<Library> libraries = libraryRepository.findAll();
        assertThat(libraries).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);
        librarySearchRepository.save(library);

        // Search the library
        restLibraryMockMvc.perform(get("/api/_search/libraries?query=id:" + library.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(library.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].internetAccess").value(hasItem(DEFAULT_INTERNET_ACCESS.booleanValue())));
    }
}
