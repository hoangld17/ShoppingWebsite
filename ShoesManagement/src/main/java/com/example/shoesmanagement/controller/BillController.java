package com.example.shoesmanagement.controller;

import com.example.shoesmanagement.dto.request.CreateBillDetailRequest;
import com.example.shoesmanagement.dto.request.SearchBillRequest;
import com.example.shoesmanagement.dto.request.SearchShoeRequest;
import com.example.shoesmanagement.dto.response.ShowDataResponse;
import com.example.shoesmanagement.exception.ApplicationException;
import com.example.shoesmanagement.model.Brand;
import com.example.shoesmanagement.model.util.Validator;
import com.example.shoesmanagement.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.shoesmanagement.model.util.ModelConstant.BRAND_NOT_FOUND;

@RestController
@RequestMapping("/bill")
@CrossOrigin(origins = "http://localhost:4200")
public class BillController {
    @Autowired
    BillService billService;
    @GetMapping("/{id}")
    public ShowDataResponse<?> getOneBill(@PathVariable("id") Long id) {
        return new ShowDataResponse<>(billService.getOneBill(id));
    }
    @GetMapping("/cart")
    public ShowDataResponse<?> getCart() {
        return new ShowDataResponse<>(billService.getCart());
    }
    @GetMapping("/quantityCart")
    public ShowDataResponse<?> getQuantityCart() {
        return new ShowDataResponse<>(billService.getQuantityCart());
    }
    @PostMapping("/addCart")
    public ShowDataResponse<?> addBillDetail(
            @RequestBody CreateBillDetailRequest createBillDetailRequest
            ) {
        billService.addBillDetail(createBillDetailRequest);
        return new ShowDataResponse<>("Adding cart successful!");
    }
    @GetMapping("/removeBillDetail/{id}")
    public ShowDataResponse<?> removeBillDetail(
            @PathVariable("id") Long id
    ) {
        billService.deleteBillDetail(id);
        return new ShowDataResponse<>("Remove bill detail successful!");
    }
    @GetMapping("/payment")
    public ShowDataResponse<?> payment() {
        billService.paymentBill();
        return new ShowDataResponse<>("Payment is successful!");
    }
    @GetMapping("/editAddress")
    public ShowDataResponse<?> editAddress(@RequestParam("address") String address) {
        billService.editAddress(address);
        return new ShowDataResponse<>("Edit address is successful!");
    }
    @GetMapping("/editPhone")
    public ShowDataResponse<?> editPhone(@RequestParam("phone") String phone) {
        billService.editPhone(phone);
        return new ShowDataResponse<>("Edit phone is successful!");
    }
    @PostMapping("/historyBill")
    public ShowDataResponse<?> getHistoryBill(
            @RequestBody SearchBillRequest searchBillRequest
    ) {
        if (searchBillRequest.getPage() < 0){
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Page cannot be negative number.");
        }
        return new ShowDataResponse<>(billService.listBillUser(searchBillRequest));
    }
}
