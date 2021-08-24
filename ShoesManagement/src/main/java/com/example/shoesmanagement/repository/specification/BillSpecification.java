package com.example.shoesmanagement.repository.specification;

import com.example.shoesmanagement.dto.request.SearchBillRequest;
import com.example.shoesmanagement.exception.ApplicationException;
import com.example.shoesmanagement.model.Bill;
import com.example.shoesmanagement.model.Brand;
import com.example.shoesmanagement.model.enums.BillType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BillSpecification {
    public Specification<Bill> doFilterBill(
            SearchBillRequest searchBillRequest,
            Long idConsumer
    ) {
        return (billRoot,
                cq,
                cb) -> {
            cq.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(billRoot.get("idConsumer"), idConsumer));
            predicates.add(cb.equal(billRoot.get("billType"), BillType.Export));
            predicates.add(cb.equal(billRoot.get("cart"), false));
            if (searchBillRequest.getFromDate() != null || searchBillRequest.getToDate() != null) {
                Date checkFromDate = null;
                Date checkToDate = new Date();
                if (searchBillRequest.getFromDate() != null && !searchBillRequest.getFromDate().isBlank()) {
                    try {
                        checkFromDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchBillRequest.getFromDate().trim());
                    } catch (ParseException e) {
                        throw new ApplicationException(HttpStatus.BAD_REQUEST, "Wrong format of fromDate (yyyy-MM-dd)");
                    }
                }
                if (searchBillRequest.getToDate() != null && !searchBillRequest.getToDate().isBlank()) {
                    try {
                        checkToDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchBillRequest.getToDate().trim());
                    } catch (ParseException e) {
                        throw new ApplicationException(HttpStatus.BAD_REQUEST, "Wrong format of fromDate (yyyy-MM-dd)");
                    }
                }
                
                if (checkFromDate == null)
                    predicates.add(cb.lessThanOrEqualTo(billRoot.get("purchaseDate"), new Date(checkToDate.getTime() + 86400000)));
                else predicates.add(cb.between(billRoot.get("purchaseDate"), checkFromDate, new Date(checkToDate.getTime() + 86400000)));
            }
            if (searchBillRequest.isLatest()) {
                cq.orderBy(cb.desc(billRoot.get("purchaseDate")));
            } else {
                cq.orderBy(cb.asc(billRoot.get("purchaseDate")));
            }

            return cb.and(predicates.toArray(new Predicate[]{}));

        };
    }
}
