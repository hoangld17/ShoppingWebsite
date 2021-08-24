package com.example.shoesmanagement.model;

import com.example.shoesmanagement.model.enums.BillType;
import com.example.shoesmanagement.model.util.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class Bill extends AuditableDomain<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idConsumer;
    private Date purchaseDate;
    private BillType billType;
    private String phone;
    private String address;
    private boolean cart;
    private double total;
    private double discount;
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = Validator.convertDate(purchaseDate, "Purchase date");
    }
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setPhone(String phone) {
        Validator.checkPhoneFormat(phone);
        this.phone = phone;
    }

    public void setAddress(String address) {
        Validator.checkNullEmptyAndLength(address, 200, "Address");
        this.address = address;
    }

    public void setCart(boolean cart) {
        this.cart = cart;
    }
}
