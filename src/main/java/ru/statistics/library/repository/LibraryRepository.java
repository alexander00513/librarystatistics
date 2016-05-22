package ru.statistics.library.repository;

import ru.statistics.library.domain.Library;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Library entity.
 */
@SuppressWarnings("unused")
public interface LibraryRepository extends JpaRepository<Library,Long> {

}
