package ru.statistics.library.repository;

import ru.statistics.library.domain.Borough;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Borough entity.
 */
@SuppressWarnings("unused")
public interface BoroughRepository extends JpaRepository<Borough,Long> {

}
