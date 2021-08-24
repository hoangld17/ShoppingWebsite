package com.example.shoesmanagement.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateConsumerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
