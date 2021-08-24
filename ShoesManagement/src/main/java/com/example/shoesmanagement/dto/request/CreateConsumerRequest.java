package com.example.shoesmanagement.dto.request;

import com.example.shoesmanagement.model.util.ParamError;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class CreateConsumerRequest {
    @NotBlank(message = ParamError.FIELD_NAME)
    private String firstName;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String lastName;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String email;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String phone;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String address;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String username;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String password;

    @NotBlank(message = ParamError.FIELD_NAME)
    private String confirmed;

}
