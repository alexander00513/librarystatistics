package ru.statistics.library.repository.search;

import ru.statistics.library.domain.Publication;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Publication entity.
 */
public interface PublicationSearchRepository extends ElasticsearchRepository<Publication, Long> {
}
