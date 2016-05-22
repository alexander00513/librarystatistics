package ru.statistics.library.repository.search;

import ru.statistics.library.domain.Equipment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Equipment entity.
 */
public interface EquipmentSearchRepository extends ElasticsearchRepository<Equipment, Long> {
}
