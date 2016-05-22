package ru.statistics.library.repository;

import ru.statistics.library.domain.PublicationsRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PublicationsRequest entity.
 */
@SuppressWarnings("unused")
public interface PublicationsRequestRepository extends JpaRepository<PublicationsRequest,Long> {

}
