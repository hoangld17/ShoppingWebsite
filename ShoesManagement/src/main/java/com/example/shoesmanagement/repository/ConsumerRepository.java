package com.example.shoesmanagement.repository;

import com.example.shoesmanagement.model.Consumer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ConsumerRepository extends PagingAndSortingRepository<Consumer, Long>, JpaSpecificationExecutor<Consumer> {
    Consumer findByUsername(String userName);
    boolean existsByUsername(String username);
}
