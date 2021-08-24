package com.example.shoesmanagement.model;

import com.example.shoesmanagement.model.enums.AppStatus;
import com.example.shoesmanagement.model.util.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dom4j.Branch;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class Shoe extends AuditableDomain<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idBrand;
    private String name;
    private String description;
    private double price;
    private double discount;
    private int totalImages;
    private AppStatus status;

    public void setTotalImages(int totalImages) {
        Validator.checkNumber(totalImages, "Total images");
        this.totalImages = totalImages;
    }

    public void setName(String name) {
        Validator.checkNullEmptyAndLength(name, 100, "Name");
        this.name = name;
    }

    public void setPrice(double price) {
        Validator.checkNumber(price, "Price");
        this.price = price;
    }

    public void setStatus(AppStatus status) {
        Validator.checkNull(status, "Status");
        this.status = status;
    }

    public void setDescription(String description) {
        Validator.checkNullEmptyAndLength(name, 255, "Description");
        this.description = description;
    }

    public void setDiscount(double discount) {
        Validator.checkNumber(discount, "Discount price");
        this.discount = discount;
    }
}
