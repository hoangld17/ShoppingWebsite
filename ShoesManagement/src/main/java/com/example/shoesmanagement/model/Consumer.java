package com.example.shoesmanagement.model;

import com.example.shoesmanagement.model.enums.UserRole;
import com.example.shoesmanagement.model.util.Constant;
import com.example.shoesmanagement.model.util.Validator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class Consumer implements Serializable {
    UserRole role;
    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date updatedDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String username;
    @JsonProperty(access = WRITE_ONLY)
    private String passwordHash;
    @JsonProperty(access = WRITE_ONLY)
    private String passwordSalt;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        this.updatedDate = null;

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date();
    }

    public void setFirstName(String firstName) {
        Validator.checkNullEmptyAndLength(firstName, 50, "First name");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        Validator.checkNullEmptyAndLength(lastName, 50, "Last name");
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        Validator.validateEmail(email);
        this.email = email;
    }

    public void setPhone(String phone) {
        Validator.checkPhoneFormat(phone);
        this.phone = phone;
    }

    public void setAddress(String address) {
        Validator.checkNullEmptyAndLength(address, 200, "Address");
        this.address = address;
    }

    public void setUsername(String username) {
        Validator.checkNullEmptyAndLength(username, 50, "Username");
        this.username = username;
    }
}
