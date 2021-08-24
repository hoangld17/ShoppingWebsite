package com.example.shoesmanagement.dto.request;

import com.example.shoesmanagement.model.util.ParamError;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
@Data
@NoArgsConstructor
public class SearchShoeRequest {
    @NotNull(message = ParamError.FIELD_NAME)
    private int page;
    @NotNull(message = ParamError.FIELD_NAME)
    private int size;
    private String search;
    private List<Long> idBrands;
    private double minPrice;
    private double maxPrice;
    private String sortField;
    private boolean sortType;

}
