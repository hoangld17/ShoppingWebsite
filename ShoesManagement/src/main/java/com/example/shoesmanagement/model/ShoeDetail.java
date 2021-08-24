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
public class ShoeDetail extends AuditableDomain<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idShoe;
    private double size;
    private int currentQuantity;

    public void setSize(double size) {
        if (size > 45 || size < 35)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Size of shoes is between 35 and 45.");
        this.size = size;
    }

    public void setCurrentQuantity(int currentQuantity) {
        Validator.checkNumber(currentQuantity, "Current quantity");
        this.currentQuantity = currentQuantity;
    }
}
