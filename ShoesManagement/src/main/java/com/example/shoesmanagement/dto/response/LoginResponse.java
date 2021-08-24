package com.example.shoesmanagement.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String token;
    private String image;
}
