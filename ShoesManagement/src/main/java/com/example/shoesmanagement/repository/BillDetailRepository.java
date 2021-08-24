package com.example.shoesmanagement.repository;

import com.example.shoesmanagement.model.BillDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BillDetailRepository extends PagingAndSortingRepository<BillDetail, Long>, JpaSpecificationExecutor<BillDetail> {
    List<BillDetail> findByIdBill(Long idBill);
}
