package com.example.shoesmanagement.dto.response;

import com.example.shoesmanagement.model.enums.AppStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ShoeResponse {
    private Long id;
    private String name;
    private double price;
    private String description;
    private String image;
    private double discount;
}
