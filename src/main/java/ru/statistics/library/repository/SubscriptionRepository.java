package ru.statistics.library.repository;

import ru.statistics.library.domain.Subscription;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Subscription entity.
 */
@SuppressWarnings("unused")
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

}
