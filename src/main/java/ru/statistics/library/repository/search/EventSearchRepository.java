package ru.statistics.library.repository.search;

import ru.statistics.library.domain.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Event entity.
 */
public interface EventSearchRepository extends ElasticsearchRepository<Event, Long> {
}
