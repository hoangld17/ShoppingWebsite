package com.example.shoesmanagement.repository;

import com.example.shoesmanagement.model.ShoeDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ShoesDetailRepository extends PagingAndSortingRepository<ShoeDetail, Long>, JpaSpecificationExecutor<ShoeDetail> {
    List<ShoeDetail> findAllByIdShoe(Long id);
}
