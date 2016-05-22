package ru.statistics.library.repository.search;

import ru.statistics.library.domain.Library;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Library entity.
 */
public interface LibrarySearchRepository extends ElasticsearchRepository<Library, Long> {
}
