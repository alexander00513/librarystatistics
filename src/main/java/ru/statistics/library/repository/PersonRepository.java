package ru.statistics.library.repository;

import ru.statistics.library.domain.Person;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
public interface PersonRepository extends JpaRepository<Person,Long> {

    @Query("select distinct person from Person person left join fetch person.libraries left join fetch person.events")
    List<Person> findAllWithEagerRelationships();

    @Query("select person from Person person left join fetch person.libraries left join fetch person.events where person.id =:id")
    Person findOneWithEagerRelationships(@Param("id") Long id);

}
