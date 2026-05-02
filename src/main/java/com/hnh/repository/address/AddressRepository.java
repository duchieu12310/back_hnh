package com.hnh.repository.address;

import com.hnh.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isDefault = true")
    Optional<Address> findDefaultAddressByUserId(Long userId);
}

