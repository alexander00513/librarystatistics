package ru.statistics.library.repository;

import ru.statistics.library.domain.Publication;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Publication entity.
 */
@SuppressWarnings("unused")
public interface PublicationRepository extends JpaRepository<Publication,Long> {

}
