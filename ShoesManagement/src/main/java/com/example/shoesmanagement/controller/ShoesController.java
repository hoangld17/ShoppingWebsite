package com.example.shoesmanagement.controller;

import com.example.shoesmanagement.controller.helper.MappingHelper;
import com.example.shoesmanagement.dto.request.CreateShoeRequest;
import com.example.shoesmanagement.dto.request.SearchShoeRequest;
import com.example.shoesmanagement.dto.response.ShoeResponse;
import com.example.shoesmanagement.dto.response.ShowDataResponse;
import com.example.shoesmanagement.exception.ApplicationException;
import com.example.shoesmanagement.model.Shoe;
import com.example.shoesmanagement.service.ShoesService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import static com.example.shoesmanagement.model.util.ModelConstant.SHOE_NOT_FOUND;

@RestController
@RequestMapping("/shoes")
@CrossOrigin(origins = "http://localhost:4200")
public class ShoesController {
    @Autowired
    ShoesService shoesService;

    @PostMapping()
    public ShowDataResponse<?> createShoe(
            @RequestBody CreateShoeRequest createShoeRequest
    ) {
        return new ShowDataResponse<>(shoesService.saveShoe(MappingHelper.mapShoe(createShoeRequest), MappingHelper.mapShoeDetail(createShoeRequest), createShoeRequest.getImages()));
    }
    @GetMapping("/{id}")
    public ShowDataResponse<?> getShoeById(
            @PathVariable("id") Long id
    ) {
        return new ShowDataResponse<>(shoesService.getShoeById(id));
    }
    @GetMapping("/idBrand/{id}")
    public ShowDataResponse<?> getShoeByIdBrand(
            @PathVariable("id") Long id
    ) {
        return new ShowDataResponse<>(shoesService.getShoeByIdBrand(id));
    }
    @GetMapping("/list")
    public ShowDataResponse<?> getAllShoe(
    ) {
        return new ShowDataResponse<>(shoesService.getAllShoe());
    }
    @GetMapping("/quantityPerBrand")
    public ShowDataResponse<?> getQuantityPerBrand(
    ) {
        return new ShowDataResponse<>(shoesService.getQuantityPerBrand());
    }

    @PostMapping("/pages")
    public ShowDataResponse<?> getPagingShoe(
            @RequestBody SearchShoeRequest searchShoeRequest
            ) {
        if (searchShoeRequest.getPage() <= 0){
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Page must be greater than 0.");
        }
        if (searchShoeRequest.getSize() <= 0){
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Size must be greater than 0.");
        }
        return new ShowDataResponse<>(shoesService.getPagingShoes(searchShoeRequest));
    }
}
