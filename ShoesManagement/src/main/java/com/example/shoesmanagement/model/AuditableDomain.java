package com.example.shoesmanagement.model;

import com.example.shoesmanagement.model.util.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//lombok annotations
@Data
@ToString

//spring auditing annotations
//annotation designates a class whose mapping information is applied to the
//entities that inherit from it. A mapped super class has no separate table defined
//for it
@MappedSuperclass
//specifies the callback listener classes to be used for an entity or mapped
//superclass
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuditableDomain<U> implements Serializable {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private U createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "updated_by")
    private U updatedBy;

    @LastModifiedDate
    @Column(name = "updated_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.API_FORMAT_DATE_TIME)
    private Date updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
        this.updatedDate = null;
        this.updatedDate = null;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date();
    }
}
