package com.example.shoesmanagement.service.implement;

import com.example.shoesmanagement.dto.request.CreateBillDetailRequest;
import com.example.shoesmanagement.dto.request.SearchBillRequest;
import com.example.shoesmanagement.dto.response.BillDetailUserResponse;
import com.example.shoesmanagement.dto.response.BillUserResponse;
import com.example.shoesmanagement.dto.response.OneBillUserResponse;
import com.example.shoesmanagement.exception.ApplicationException;
import com.example.shoesmanagement.model.Bill;
import com.example.shoesmanagement.model.BillDetail;
import com.example.shoesmanagement.model.Consumer;
import com.example.shoesmanagement.model.ShoeDetail;
import com.example.shoesmanagement.model.enums.BillType;
import com.example.shoesmanagement.repository.BillDetailRepository;
import com.example.shoesmanagement.repository.BillRepository;
import com.example.shoesmanagement.repository.specification.BillSpecification;
import com.example.shoesmanagement.repository.specification.ShoeSpecification;
import com.example.shoesmanagement.service.BillService;
import com.example.shoesmanagement.service.BrandService;
import com.example.shoesmanagement.service.ConsumerService;
import com.example.shoesmanagement.service.ShoesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillServiceImp implements BillService {
    @Autowired
    BillRepository billRepository;

    @Autowired
    BillDetailRepository billDetailRepository;

    @Autowired
    ConsumerService consumerService;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ShoesService shoesService;

    @Autowired
    BrandService brandService;

    @Autowired
    private BillSpecification billSpecification;

    @Override
    public OneBillUserResponse getCart() {
        Bill bill = getCartUser();
        OneBillUserResponse oneBillUserResponse = modelMapper.map(bill, OneBillUserResponse.class);
        List<BillDetail> billDetailList = billDetailRepository.findByIdBill(bill.getId());
        if (billDetailList.isEmpty())
            return oneBillUserResponse;
        List<BillDetailUserResponse> list = billDetailList.stream().map(x -> modelMapper.map(x, BillDetailUserResponse.class)).collect(Collectors.toList());
        shoesService.transferData(list);
        oneBillUserResponse.setListBillDetail(list);
        return oneBillUserResponse;
    }
    public OneBillUserResponse getOneBill(Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Consumer consumer = consumerService.getConsumerByUsername(username);
        Bill bill = billRepository.findOneByIdAndIdConsumer(id, consumer.getId());
        if (bill == null){
            throw new ApplicationException(HttpStatus.NOT_FOUND, "No bill found");
        }
        OneBillUserResponse oneBillUserResponse = modelMapper.map(bill, OneBillUserResponse.class);
        List<BillDetail> billDetailList = billDetailRepository.findByIdBill(bill.getId());
        List<BillDetailUserResponse> list = billDetailList.stream().map(x -> modelMapper.map(x, BillDetailUserResponse.class)).collect(Collectors.toList());
        shoesService.transferData(list);
        oneBillUserResponse.setListBillDetail(list);
        return oneBillUserResponse;
    }
    @Override
    public int getQuantityCart() {
        Bill bill = getCartUser();
        return billDetailRepository.findByIdBill(bill.getId()).stream().mapToInt(x -> x.getQuantity()).sum();
    }
    public Bill saveBill(Bill bill){
       return billRepository.save(bill);
    }
    public BillDetail saveBillDetail(BillDetail billDetail){
        return billDetailRepository.save(billDetail);
    }
    @Override
    public Bill createBillEmptyUser(Consumer consumer){
        Bill bill = new Bill();
        bill.setIdConsumer(consumer.getId());
        bill.setBillType(BillType.Export);
        bill.setCart(true);
        bill.setAddress(consumer.getAddress());
        bill.setPhone(consumer.getPhone());
        bill.setTotal(0);
        bill.setDiscount(0);
        return saveBill(bill);
    }
    @Override
    public void addBillDetail(CreateBillDetailRequest createBillDetailRequest){
        Bill bill = getCartUser();
        BillDetail billDetail = new BillDetail();
        billDetail.setIdBill(bill.getId());
        billDetail.setIdShoeDetail(createBillDetailRequest.getIdShoeDetail());
        billDetail.setQuantity(createBillDetailRequest.getQuantity());
        billDetail.setPrice(createBillDetailRequest.getPrice());
        bill.setTotal(billDetail.getPrice() * billDetail.getQuantity() + bill.getTotal());
        saveBill(bill);
        saveBillDetail(billDetail);
    }
    @Override
    public Page<BillUserResponse> listBillUser(SearchBillRequest searchBillRequest){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Consumer consumer = consumerService.getConsumerByUsername(username);
        Specification<Bill> specification = billSpecification.doFilterBill(searchBillRequest, consumer.getId());
        Pageable pageable = PageRequest.of(searchBillRequest.getPage(), 9);
        return billRepository.findAll(specification, pageable).map(x -> modelMapper.map(x, BillUserResponse.class));
    }
    @Override
    public void paymentBill(){
        Bill bill = getCartUser();
        List<BillDetail> billDetails = billDetailRepository.findByIdBill(bill.getId());
        if (billDetails.isEmpty()){
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Add at least one pair of shoes to payment!");
        }
        billDetails.forEach(x -> {
            ShoeDetail shoeDetail = shoesService.getShoeDetailById(x.getIdShoeDetail());
            int newQuantity = shoeDetail.getCurrentQuantity() - x.getQuantity();
            if (newQuantity < 0){
                throw new ApplicationException(HttpStatus.BAD_REQUEST, shoesService.getShoeById(shoeDetail.getIdShoe()).getName() + " has "+ shoeDetail.getCurrentQuantity()+" shoes in stock.");
            }
            shoeDetail.setCurrentQuantity(newQuantity);
            shoesService.saveShoeDetail(shoeDetail);
        });
        bill.setCart(false);
        bill.setPurchaseDate(new Date());
        saveBill(bill);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Consumer consumer = consumerService.getConsumerByUsername(username);
        createBillEmptyUser(consumer);
    }
    @Override
    public void editAddress(String address){
        Bill bill = getCartUser();
        bill.setAddress(address);
        saveBill(bill);
    }
    @Override
    public void editPhone(String phone){
        Bill bill = getCartUser();
        bill.setPhone(phone);
        saveBill(bill);
    }

    public BillDetail getBillDetailById(Long id) { return billDetailRepository.findById(id).get(); }
    @Override
    public void deleteBillDetail(Long id){
        Bill bill = getCartUser();
        BillDetail billDetail = getBillDetailById(id);
        bill.setTotal(bill.getTotal() - billDetail.getPrice() * billDetail.getQuantity());
        saveBill(bill);
        billDetailRepository.delete(billDetail);
    }
    private Bill getCartUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Consumer consumer = consumerService.getConsumerByUsername(username);
        return billRepository.findOneByIdConsumerAndBillTypeAndCart(consumer.getId(), BillType.Export, true);
    }
}
