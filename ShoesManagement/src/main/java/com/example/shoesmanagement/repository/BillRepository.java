package com.example.shoesmanagement.repository;

import com.example.shoesmanagement.model.Bill;
import com.example.shoesmanagement.model.enums.BillType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BillRepository extends PagingAndSortingRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {
    Bill findOneByIdConsumerAndBillTypeAndCart(Long idConsumer, BillType billType, boolean cart);
    Bill findOneByIdAndIdConsumer(Long id, Long idConsumer);
}
