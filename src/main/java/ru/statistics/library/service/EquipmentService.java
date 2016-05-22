package ru.statistics.library.service;

import ru.statistics.library.domain.Equipment;
import ru.statistics.library.web.rest.dto.EquipmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Equipment.
 */
public interface EquipmentService {

    /**
     * Save a equipment.
     * 
     * @param equipmentDTO the entity to save
     * @return the persisted entity
     */
    EquipmentDTO save(EquipmentDTO equipmentDTO);

    /**
     *  Get all the equipment.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Equipment> findAll(Pageable pageable);

    /**
     *  Get the "id" equipment.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    EquipmentDTO findOne(Long id);

    /**
     *  Delete the "id" equipment.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the equipment corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Equipment> search(String query, Pageable pageable);
}
