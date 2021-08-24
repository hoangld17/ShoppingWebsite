package com.example.shoesmanagement.service;

import com.example.shoesmanagement.model.Brand;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {
    Brand saveBrand(Brand clazz);

    Brand getBrandById(Long id);

    List<Brand> getAllBrand();

    Brand deleteBrand(Long id);

    Page<Brand> getPagingBrand(String name, String search, int page, int size, boolean sort, String sortField);

}
