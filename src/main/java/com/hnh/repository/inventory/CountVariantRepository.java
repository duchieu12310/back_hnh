package com.hnh.repository.inventory;

import com.hnh.entity.inventory.CountVariant;
import com.hnh.entity.inventory.CountVariantKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CountVariantRepository extends JpaRepository<CountVariant, CountVariantKey>,
        JpaSpecificationExecutor<CountVariant> {}

