package com.example.shoesmanagement.dto.request;

import com.example.shoesmanagement.model.util.ParamError;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UpdateBrandRequest {
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String name;
}