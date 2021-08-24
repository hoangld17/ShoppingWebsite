package com.example.shoesmanagement.model;

import com.example.shoesmanagement.model.enums.AppStatus;
import com.example.shoesmanagement.model.util.Validator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Brand extends AuditableDomain<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public void setName(String name) {
        Validator.checkNullEmptyAndLength(name, 64, "Name");
        this.name = name;
    }
}
