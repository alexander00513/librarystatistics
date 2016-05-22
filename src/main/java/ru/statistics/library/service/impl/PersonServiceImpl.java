package ru.statistics.library.service.impl;

import ru.statistics.library.service.PersonService;
import ru.statistics.library.domain.Person;
import ru.statistics.library.repository.PersonRepository;
import ru.statistics.library.repository.search.PersonSearchRepository;
import ru.statistics.library.web.rest.dto.PersonDTO;
import ru.statistics.library.web.rest.mapper.PersonMapper;
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
 * Service Implementation for managing Person.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService{

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);
    
    @Inject
    private PersonRepository personRepository;
    
    @Inject
    private PersonMapper personMapper;
    
    @Inject
    private PersonSearchRepository personSearchRepository;
    
    /**
     * Save a person.
     * 
     * @param personDTO the entity to save
     * @return the persisted entity
     */
    public PersonDTO save(PersonDTO personDTO) {
        log.debug("Request to save Person : {}", personDTO);
        Person person = personMapper.personDTOToPerson(personDTO);
        person = personRepository.save(person);
        PersonDTO result = personMapper.personToPersonDTO(person);
        personSearchRepository.save(person);
        return result;
    }

    /**
     *  Get all the people.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Person> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        Page<Person> result = personRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one person by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PersonDTO findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        Person person = personRepository.findOneWithEagerRelationships(id);
        PersonDTO personDTO = personMapper.personToPersonDTO(person);
        return personDTO;
    }

    /**
     *  Delete the  person by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.delete(id);
        personSearchRepository.delete(id);
    }

    /**
     * Search for the person corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Person> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of People for query {}", query);
        return personSearchRepository.search(queryStringQuery(query), pageable);
    }
}
