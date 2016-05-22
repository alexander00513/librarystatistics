package ru.statistics.library.repository.search;

import ru.statistics.library.domain.Website;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Website entity.
 */
public interface WebsiteSearchRepository extends ElasticsearchRepository<Website, Long> {
}
