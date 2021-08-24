package com.example.shoesmanagement.service;

import com.example.shoesmanagement.dto.request.UpdateConsumerRequest;
import com.example.shoesmanagement.dto.response.LoginResponse;
import com.example.shoesmanagement.model.Consumer;

public interface ConsumerService {
    LoginResponse signin(String username, String password);

    void saveConsumer(Consumer consumer);
    Consumer getConsumerByUsername(String username);

    void signup(Consumer consumer);
    void changePassword(String oldPassword, String newPassword);
    void updateInfoUser(UpdateConsumerRequest updateConsumerRequest);
    void updateAvatar(String image);
}
