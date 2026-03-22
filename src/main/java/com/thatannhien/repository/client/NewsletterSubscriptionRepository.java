package com.thatannhien.repository.client;

import com.thatannhien.entity.client.NewsletterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscription, Long>, JpaSpecificationExecutor<NewsletterSubscription> {
    Optional<NewsletterSubscription> findByEmail(String email);
    boolean existsByEmail(String email);
}

