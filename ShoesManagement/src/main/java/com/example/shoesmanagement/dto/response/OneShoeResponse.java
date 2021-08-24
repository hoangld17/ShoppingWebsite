package com.example.shoesmanagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class OneShoeResponse {
    private Long id;
    private String name;
    private Long idBrand;
    private String nameBrand;
    private double price;
    private String description;
    private List<String> images;
    private double discount;
    List<ShoeDetailResponse> shoeDetailResponses;
}
