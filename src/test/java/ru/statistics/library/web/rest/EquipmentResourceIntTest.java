package ru.statistics.library.web.rest;

import ru.statistics.library.LibrarystatisticsApp;
import ru.statistics.library.domain.Equipment;
import ru.statistics.library.repository.EquipmentRepository;
import ru.statistics.library.service.EquipmentService;
import ru.statistics.library.repository.search.EquipmentSearchRepository;
import ru.statistics.library.web.rest.dto.EquipmentDTO;
import ru.statistics.library.web.rest.mapper.EquipmentMapper;

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

import ru.statistics.library.domain.enumeration.EquipmentType;

/**
 * Test class for the EquipmentResource REST controller.
 *
 * @see EquipmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LibrarystatisticsApp.class)
@WebAppConfiguration
@IntegrationTest
public class EquipmentResourceIntTest {

    private static final String DEFAULT_UID = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DECRIPTION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DECRIPTION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final EquipmentType DEFAULT_TYPE = EquipmentType.COMPUTER;
    private static final EquipmentType UPDATED_TYPE = EquipmentType.ROUTER;

    @Inject
    private EquipmentRepository equipmentRepository;

    @Inject
    private EquipmentMapper equipmentMapper;

    @Inject
    private EquipmentService equipmentService;

    @Inject
    private EquipmentSearchRepository equipmentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EquipmentResource equipmentResource = new EquipmentResource();
        ReflectionTestUtils.setField(equipmentResource, "equipmentService", equipmentService);
        ReflectionTestUtils.setField(equipmentResource, "equipmentMapper", equipmentMapper);
        this.restEquipmentMockMvc = MockMvcBuilders.standaloneSetup(equipmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        equipmentSearchRepository.deleteAll();
        equipment = new Equipment();
        equipment.setUid(DEFAULT_UID);
        equipment.setDecription(DEFAULT_DECRIPTION);
        equipment.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
                .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipment.get(equipment.size() - 1);
        assertThat(testEquipment.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testEquipment.getDecription()).isEqualTo(DEFAULT_DECRIPTION);
        assertThat(testEquipment.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the Equipment in ElasticSearch
        Equipment equipmentEs = equipmentSearchRepository.findOne(testEquipment.getId());
        assertThat(equipmentEs).isEqualToComparingFieldByField(testEquipment);
    }

    @Test
    @Transactional
    public void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setUid(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
                .andExpect(status().isBadRequest());

        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setType(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
                .andExpect(status().isBadRequest());

        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipment
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
                .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
                .andExpect(jsonPath("$.[*].decription").value(hasItem(DEFAULT_DECRIPTION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.decription").value(DEFAULT_DECRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        equipmentSearchRepository.save(equipment);
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = new Equipment();
        updatedEquipment.setId(equipment.getId());
        updatedEquipment.setUid(UPDATED_UID);
        updatedEquipment.setDecription(UPDATED_DECRIPTION);
        updatedEquipment.setType(UPDATED_TYPE);
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(updatedEquipment);

        restEquipmentMockMvc.perform(put("/api/equipment")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
                .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipment.get(equipment.size() - 1);
        assertThat(testEquipment.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testEquipment.getDecription()).isEqualTo(UPDATED_DECRIPTION);
        assertThat(testEquipment.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the Equipment in ElasticSearch
        Equipment equipmentEs = equipmentSearchRepository.findOne(testEquipment.getId());
        assertThat(equipmentEs).isEqualToComparingFieldByField(testEquipment);
    }

    @Test
    @Transactional
    public void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        equipmentSearchRepository.save(equipment);
        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Get the equipment
        restEquipmentMockMvc.perform(delete("/api/equipment/{id}", equipment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean equipmentExistsInEs = equipmentSearchRepository.exists(equipment.getId());
        assertThat(equipmentExistsInEs).isFalse();

        // Validate the database is empty
        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        equipmentSearchRepository.save(equipment);

        // Search the equipment
        restEquipmentMockMvc.perform(get("/api/_search/equipment?query=id:" + equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].decription").value(hasItem(DEFAULT_DECRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
}
