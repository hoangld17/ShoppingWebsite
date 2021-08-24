package com.example.shoesmanagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillDetailUserResponse {
    private Long id;
    private int quantity;
    private double price;
    private Long idShoeDetail;
    private Long idShoes;
    private double size;
    private String nameShoes;
    private String nameBrand;
    private String image;
}
