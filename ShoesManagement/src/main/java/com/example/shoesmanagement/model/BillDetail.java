package com.example.shoesmanagement.model;

import com.example.shoesmanagement.exception.ApplicationException;
import com.example.shoesmanagement.model.util.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.http.HttpStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class BillDetail extends AuditableDomain<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idBill;
    private Long idShoeDetail;
    private int quantity;
    private double price;

    public void setQuantity(int quantity) {
        Validator.checkNumber(quantity, "Quantity");
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        Validator.checkNumber(price, "Price");
        this.price = price;
    }
}
