package ru.statistics.library.repository.search;

import ru.statistics.library.domain.Subscription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Subscription entity.
 */
public interface SubscriptionSearchRepository extends ElasticsearchRepository<Subscription, Long> {
}
