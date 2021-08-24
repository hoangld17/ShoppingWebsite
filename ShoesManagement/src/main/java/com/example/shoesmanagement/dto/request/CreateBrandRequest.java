package com.example.shoesmanagement.dto.request;

import com.example.shoesmanagement.model.util.ParamError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBrandRequest {
    @NotBlank(message = ParamError.FIELD_NAME)
    @Size(max = 64, message = ParamError.MAX_LENGTH)
    private String name;
}
