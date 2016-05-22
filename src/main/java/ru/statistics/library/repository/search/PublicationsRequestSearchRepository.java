package ru.statistics.library.repository.search;

import ru.statistics.library.domain.PublicationsRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PublicationsRequest entity.
 */
public interface PublicationsRequestSearchRepository extends ElasticsearchRepository<PublicationsRequest, Long> {
}
