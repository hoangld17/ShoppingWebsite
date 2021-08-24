package com.example.shoesmanagement.controller.helper;

import com.example.shoesmanagement.dto.request.CreateConsumerRequest;
import com.example.shoesmanagement.dto.request.CreateShoeRequest;
import com.example.shoesmanagement.dto.request.UpdateConsumerRequest;
import com.example.shoesmanagement.model.Consumer;
import com.example.shoesmanagement.model.Shoe;
import com.example.shoesmanagement.model.ShoeDetail;
import com.example.shoesmanagement.model.enums.AppStatus;
import com.example.shoesmanagement.model.enums.UserRole;
import com.example.shoesmanagement.model.util.AppUtil;
import com.example.shoesmanagement.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MappingHelper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BrandService brandService;

    public static Consumer mapConsumer(Consumer consumer, UpdateConsumerRequest updateConsumerRequest) {
        if (updateConsumerRequest.getFirstName() != null && !updateConsumerRequest.getFirstName().isBlank())
            consumer.setFirstName(updateConsumerRequest.getFirstName());
        if (updateConsumerRequest.getLastName() != null && !updateConsumerRequest.getLastName().isBlank())
            consumer.setLastName(updateConsumerRequest.getLastName());
        if (updateConsumerRequest.getEmail() != null && !updateConsumerRequest.getEmail().isBlank())
            consumer.setEmail(updateConsumerRequest.getEmail());
        if (updateConsumerRequest.getPhone() != null && !updateConsumerRequest.getPhone().isBlank())
            consumer.setPhone(updateConsumerRequest.getPhone());
        if (updateConsumerRequest.getAddress() != null && !updateConsumerRequest.getAddress().isBlank())
            consumer.setAddress(updateConsumerRequest.getAddress());
        return consumer;
    }

    public Consumer mapConsumer(CreateConsumerRequest createConsumerRequest) {
        Consumer consumer = new Consumer();
        consumer.setFirstName(createConsumerRequest.getFirstName());
        consumer.setLastName(createConsumerRequest.getLastName());
        consumer.setEmail(createConsumerRequest.getEmail());
        consumer.setPhone(createConsumerRequest.getPhone());
        consumer.setAddress(createConsumerRequest.getAddress());
        consumer.setUsername(createConsumerRequest.getUsername());
        String passwordSalt = AppUtil.generateSalt();
        consumer.setPasswordHash(passwordEncoder.encode(createConsumerRequest.getPassword().concat(passwordSalt)));
        consumer.setPasswordSalt(passwordSalt);
        consumer.setRole(UserRole.USER);
        return consumer;
    }
    public static Shoe mapShoe(CreateShoeRequest createShoeRequest){
        Shoe shoe = new Shoe();
        shoe.setIdBrand(createShoeRequest.getIdBrand());
        shoe.setName(createShoeRequest.getName());
        shoe.setPrice(createShoeRequest.getPrice());
        shoe.setDescription(createShoeRequest.getDescription());
        shoe.setDiscount(createShoeRequest.getDiscount());
        shoe.setTotalImages(4);
        shoe.setStatus(AppStatus.ACTIVE);
        return shoe;
    }
    public static List<ShoeDetail> mapShoeDetail(CreateShoeRequest createShoeRequest){
        List<ShoeDetail> list = new ArrayList<>();
        for (double size : createShoeRequest.getSizes()) {
            ShoeDetail shoeDetail = new ShoeDetail();
            shoeDetail.setCurrentQuantity(0);
            shoeDetail.setSize(size);
            list.add(shoeDetail);
        }
        return list;
    }
}
