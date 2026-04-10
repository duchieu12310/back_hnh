package com.hnh.repository;

import com.hnh.entity.product.Variant;
import com.hnh.projection.product.SimpleProductInventory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@AllArgsConstructor
public class ProjectionRepository {

    private final EntityManager em;

    public List<SimpleProductInventory> findSimpleProductInventories(List<Long> productIds) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SimpleProductInventory> query = cb.createQuery(SimpleProductInventory.class);

        Root<Variant> variant = query.from(Variant.class);

        query.select(cb.construct(
                SimpleProductInventory.class,
                variant.get("product").get("id"),
                cb.sum(variant.get("quantity")), // inventory
                cb.literal(0L), // waitingForDelivery placeholder
                cb.sum(variant.get("quantity")), // canBeSold
                cb.literal(0L) // areComing placeholder
        ));

        query.where(variant.get("product").get("id").in(productIds));
        query.groupBy(variant.get("product").get("id"));

        return em.createQuery(query).getResultList();
    }

}

