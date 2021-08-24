package com.example.shoesmanagement.service.implement;

import com.example.shoesmanagement.dto.request.SearchShoeRequest;
import com.example.shoesmanagement.dto.response.*;
import com.example.shoesmanagement.exception.ApplicationException;
import com.example.shoesmanagement.model.Brand;
import com.example.shoesmanagement.model.Shoe;
import com.example.shoesmanagement.model.ShoeDetail;
import com.example.shoesmanagement.model.enums.AppStatus;
import com.example.shoesmanagement.model.util.ModelConstant;
import com.example.shoesmanagement.model.util.Validator;
import com.example.shoesmanagement.repository.ShoesDetailRepository;
import com.example.shoesmanagement.repository.ShoesRepository;
import com.example.shoesmanagement.repository.specification.ShoeSpecification;
import com.example.shoesmanagement.service.BrandService;
import com.example.shoesmanagement.service.ShoesService;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoesServiceImp implements ShoesService {
    @Autowired
    private ShoesRepository shoeRepository;
    @Autowired
    private ShoesDetailRepository shoesDetailRepository;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ShoeSpecification shoeSpecification;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${file.upload-dir-shoes}")
    private String UPLOADED_FOLDER_SHOES;
    private Specification<Shoe> specification;


    @Override
    public OneShoeResponse saveShoe(Shoe shoe, List<ShoeDetail> shoeDetails, List<String> images) {
        brandService.getBrandById(shoe.getIdBrand());
        Shoe newShoe = shoeRepository.save(shoe);
        shoeDetails.forEach(x -> x.setIdShoe(newShoe.getId()));
        shoeDetails.forEach(x -> x.setCurrentQuantity(30));
        shoeDetails = shoeDetails.stream().map(x -> shoesDetailRepository.save(x)).collect(Collectors.toList());
//        for (int i = 0; i < images.size(); i++) {
//            try {
//                byte[] decodedBytes = Base64.getDecoder().decode(images.get(i));
//                FileUtils.writeByteArrayToFile(new File(UPLOADED_FOLDER_SHOES + newShoe.getId()+"_"+i+".jpg"), decodedBytes);
//            } catch (Exception e){
//
//            }
//        }
        return merge(shoe, shoeDetails);
    }

    @Override
    public List<ShoeResponse> getShoeByIdBrand(Long id) {
        brandService.getBrandById(id);
        List<Shoe> shoes = shoeRepository.findAllByIdBrand(id);
        return shoes.stream().map(x -> convertShoeResponse(x)).collect(Collectors.toList());
    }

    @Override
    public OneShoeResponse getShoeById(Long id) {
        Shoe shoe =shoeRepository.findOneById(id);
        if (shoe == null)
            throw new ApplicationException("No shoes found", HttpStatus.NOT_FOUND);
        return getOneShoes(shoe);
    }

    @Override
    public void saveShoeDetail(ShoeDetail shoeDetail) {
        shoesDetailRepository.save(shoeDetail);
    }

    @Override
    public ShoeDetail getShoeDetailById(Long id) {
        return shoesDetailRepository.findById(id).get();
    }

    @Override
    public List<ShoeResponse> getAllShoe() {
        return ((List<Shoe>) shoeRepository.findAll()).stream().map(x -> convertShoeResponse(x)).collect(Collectors.toList());
    }

    @Override
    public Page<ShoeResponse> getPagingShoes(SearchShoeRequest searchShoeRequest) {
        this.specification = shoeSpecification.doFilterShoe(searchShoeRequest.getSearch(), searchShoeRequest.getIdBrands(), searchShoeRequest.getMinPrice(), searchShoeRequest.getMaxPrice(), searchShoeRequest.isSortType(), searchShoeRequest.getSortField());
        Pageable pageable = PageRequest.of(searchShoeRequest.getPage() - 1, searchShoeRequest.getSize());
        return shoeRepository.findAll(specification, pageable).map(x -> convertShoeResponse(x));
    }
    @Override
    public List<QuantityPerBrand> getQuantityPerBrand(){
        List<QuantityPerBrand> list = new ArrayList <>();
        List<Shoe> shoes = shoeRepository.findAll(specification);
        for (Brand brand : brandService.getAllBrand()){
            Long quantity = shoes.stream().filter(x -> x.getIdBrand() == brand.getId()).count();
            if (quantity > 0) {
                list.add(new QuantityPerBrand(brand.getId(), quantity));
            }
        }
        return list;
    }
    @Override
    public void transferData(List<BillDetailUserResponse> list){
        list.forEach(x -> {
            ShoeDetail shoeDetail = shoesDetailRepository.findById(x.getIdShoeDetail()).get();
            x.setSize(shoeDetail.getSize());
            Shoe shoe = shoeRepository.findOneById(shoeDetail.getIdShoe());
            x.setIdShoes(shoe.getId());
            x.setNameShoes(shoe.getName());
            Brand brand = brandService.getBrandById(shoe.getIdBrand());
            x.setNameBrand(brand.getName());
            try {
                String nameImage = shoe.getId() + "_0";
                String lastFile =Files.exists(Paths.get(UPLOADED_FOLDER_SHOES, nameImage + ".jpg")) ? ".jpg" : ".png";
                String fileName = UPLOADED_FOLDER_SHOES + nameImage +lastFile;
                File file = new File(fileName);
                byte[] fileContent = FileUtils.readFileToByteArray(file);
                x.setImage(Base64.getEncoder().encodeToString(fileContent));
            } catch (Exception e){
            }
        });
    }

    private OneShoeResponse merge(Shoe shoe, List<ShoeDetail> shoeDetails){
        OneShoeResponse shoeResponse = modelMapper.map(shoe, OneShoeResponse.class);
        List<ShoeDetailResponse> shoeDetailResponses = shoeDetails.stream().map(x -> modelMapper.map(x, ShoeDetailResponse.class)).collect(Collectors.toList());
        shoeResponse.setNameBrand(brandService.getBrandById(shoe.getIdBrand()).getName());
        shoeResponse.setShoeDetailResponses(shoeDetailResponses);
        return shoeResponse;
    }
    private OneShoeResponse getOneShoes(Shoe shoe){
        OneShoeResponse shoeResponse = modelMapper.map(shoe, OneShoeResponse.class);
        List<ShoeDetailResponse> shoeDetailResponses = shoesDetailRepository.findAllByIdShoe(shoe.getId()).stream().map(x -> modelMapper.map(x, ShoeDetailResponse.class)).collect(Collectors.toList());
        shoeResponse.setNameBrand(brandService.getBrandById(shoe.getIdBrand()).getName());
        shoeResponse.setShoeDetailResponses(shoeDetailResponses);
        shoeResponse.setImages(getAllImages(shoe.getId(), shoe.getTotalImages()));
        return shoeResponse;
    }
    private ShoeResponse convertShoeResponse(Shoe shoe){
        ShoeResponse shoeResponse = modelMapper.map(shoe, ShoeResponse.class);;
        try {
            String nameImage = shoe.getId() + "_0";
            String lastFile =Files.exists(Paths.get(UPLOADED_FOLDER_SHOES, nameImage + ".jpg")) ? ".jpg" : ".png";
            String fileName = UPLOADED_FOLDER_SHOES + nameImage +lastFile;
            File file = new File(fileName);
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            shoeResponse.setImage(Base64.getEncoder().encodeToString(fileContent));
        } catch (Exception e){
            return shoeResponse;
        }
        return shoeResponse;
    }
    private List<String> getAllImages(Long id, int number){
        List<String> images = new ArrayList<>();
        for (int i = 0 ; i < number ; i++) {
            try {
                String nameImage = id + "_" + i;
                String lastFile =Files.exists(Paths.get(UPLOADED_FOLDER_SHOES, nameImage + ".jpg")) ? ".jpg" : ".png";
                String fileName = UPLOADED_FOLDER_SHOES + nameImage +lastFile;
                File file = new File(fileName);
                byte[] fileContent = FileUtils.readFileToByteArray(file);
                images.add(Base64.getEncoder().encodeToString(fileContent));
            } catch (Exception e){
                continue;
            }
        }
        return images;
    }

}
