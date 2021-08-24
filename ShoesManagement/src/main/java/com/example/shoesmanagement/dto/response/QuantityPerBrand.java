package com.example.shoesmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class QuantityPerBrand {
    private Long id;
    private Long quantity;
}
