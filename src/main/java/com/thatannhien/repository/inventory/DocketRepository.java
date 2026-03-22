package com.thatannhien.repository.inventory;

import com.thatannhien.entity.inventory.Docket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocketRepository extends JpaRepository<Docket, Long>, JpaSpecificationExecutor<Docket> {

    boolean existsByOrder_IdAndType(Long orderId, Integer type);

    java.util.Optional<Docket> findByOrder_IdAndType(Long orderId, Integer type);
}
