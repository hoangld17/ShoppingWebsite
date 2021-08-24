package com.example.shoesmanagement.repository;

import com.example.shoesmanagement.model.Shoe;
import com.example.shoesmanagement.model.enums.AppStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ShoesRepository extends
        PagingAndSortingRepository<Shoe, Long>,
        JpaSpecificationExecutor<Shoe> {
    Shoe findOneById(Long id);
    List<Shoe> findAllByIdBrand(Long id);
}
