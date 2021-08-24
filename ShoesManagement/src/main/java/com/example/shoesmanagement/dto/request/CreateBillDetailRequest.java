package com.example.shoesmanagement.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBillDetailRequest {
    private Long idShoeDetail;
    private int quantity;
    private double price;
}
