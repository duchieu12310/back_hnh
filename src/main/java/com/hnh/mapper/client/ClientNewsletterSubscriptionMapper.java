package com.hnh.mapper.client;

import com.hnh.dto.client.ClientNewsletterSubscriptionRequest;
import com.hnh.dto.client.ClientNewsletterSubscriptionResponse;
import com.hnh.entity.client.NewsletterSubscription;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientNewsletterSubscriptionMapper {

    public NewsletterSubscription requestToEntity(ClientNewsletterSubscriptionRequest request) {
        NewsletterSubscription entity = new NewsletterSubscription();
        entity.setEmail(request.getEmail().toLowerCase().trim());
        entity.setStatus(1); // Active
        return entity;
    }

    public ClientNewsletterSubscriptionResponse entityToResponse(NewsletterSubscription entity) {
        ClientNewsletterSubscriptionResponse response = new ClientNewsletterSubscriptionResponse();
        response.setSubscriptionId(entity.getId());
        response.setEmail(entity.getEmail());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}


