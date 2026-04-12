package com.hnh.repository.customer;

import com.hnh.entity.customer.CustomerResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerResourceRepository extends JpaRepository<CustomerResource, Long>, JpaSpecificationExecutor<CustomerResource> {
}
