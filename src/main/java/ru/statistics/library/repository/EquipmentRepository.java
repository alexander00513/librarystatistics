package ru.statistics.library.repository;

import ru.statistics.library.domain.Equipment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Equipment entity.
 */
@SuppressWarnings("unused")
public interface EquipmentRepository extends JpaRepository<Equipment,Long> {

}
