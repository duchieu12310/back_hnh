package com.hnh.repository.product;

import com.hnh.entity.product.Variant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VariantRepository extends JpaRepository<Variant, Long>, JpaSpecificationExecutor<Variant> {

    @Modifying
    @Query("UPDATE Variant v SET v.quantity = v.quantity - :qty WHERE v.id = :id AND v.quantity >= :qty")
    int decreaseQuantitySafe(@Param("id") Long id, @Param("qty") int qty);
}
