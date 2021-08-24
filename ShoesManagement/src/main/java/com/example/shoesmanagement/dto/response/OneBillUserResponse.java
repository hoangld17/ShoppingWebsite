package com.example.shoesmanagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class OneBillUserResponse {
    private Long id;
    private String purchaseDate;
    private String phone;
    private String address;
    private double total;
    private double discount;
    private List<BillDetailUserResponse> listBillDetail;
}
