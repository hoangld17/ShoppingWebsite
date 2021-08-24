package com.example.shoesmanagement.service;

import com.example.shoesmanagement.dto.request.CreateBillDetailRequest;
import com.example.shoesmanagement.dto.request.SearchBillRequest;
import com.example.shoesmanagement.dto.response.BillUserResponse;
import com.example.shoesmanagement.dto.response.OneBillUserResponse;
import com.example.shoesmanagement.model.Bill;
import com.example.shoesmanagement.model.Consumer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BillService {
    OneBillUserResponse getCart();

    Bill createBillEmptyUser(Consumer consumer);
    void addBillDetail(CreateBillDetailRequest createBillDetailRequest);
    int getQuantityCart();
    void paymentBill();
    void editAddress(String address);
    void editPhone(String phone);
    Page<BillUserResponse> listBillUser(SearchBillRequest searchBillRequest);
    void deleteBillDetail(Long id);
    OneBillUserResponse getOneBill(Long id);
}
