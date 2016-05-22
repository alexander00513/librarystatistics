package ru.statistics.library.repository.search;

import ru.statistics.library.domain.Borough;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Borough entity.
 */
public interface BoroughSearchRepository extends ElasticsearchRepository<Borough, Long> {
}
