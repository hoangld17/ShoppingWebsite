package com.example.shoesmanagement.service;

import com.example.shoesmanagement.dto.request.SearchShoeRequest;
import com.example.shoesmanagement.dto.response.BillDetailUserResponse;
import com.example.shoesmanagement.dto.response.OneShoeResponse;
import com.example.shoesmanagement.dto.response.QuantityPerBrand;
import com.example.shoesmanagement.dto.response.ShoeResponse;
import com.example.shoesmanagement.model.Shoe;
import com.example.shoesmanagement.model.ShoeDetail;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface ShoesService {
    OneShoeResponse saveShoe(Shoe shoe, List<ShoeDetail> shoeDetails, List<String> images);

    List<ShoeResponse> getShoeByIdBrand(Long id);

    OneShoeResponse getShoeById(Long id);
    void saveShoeDetail(ShoeDetail shoeDetail);

    ShoeDetail getShoeDetailById(Long id);

    List<ShoeResponse> getAllShoe();

    Page<ShoeResponse> getPagingShoes(SearchShoeRequest searchShoeRequest);

    List<QuantityPerBrand> getQuantityPerBrand();

    void transferData(List<BillDetailUserResponse> list);

}
