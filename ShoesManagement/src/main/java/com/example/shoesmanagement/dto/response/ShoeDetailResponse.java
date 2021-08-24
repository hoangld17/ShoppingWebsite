package com.example.shoesmanagement.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShoeDetailResponse {
    private Long id;
    private double size;
    private int currentQuantity;
}
