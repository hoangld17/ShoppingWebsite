package com.example.shoesmanagement.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateShoeRequest {
    private Long idBrand;
    private String name;
    private double price;
    private double discount;
    private String description;
    private List<Double> sizes;
    private List<String> images;
}
