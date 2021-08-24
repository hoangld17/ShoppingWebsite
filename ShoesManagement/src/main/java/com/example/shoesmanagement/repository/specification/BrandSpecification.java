package com.example.shoesmanagement.repository.specification;

import com.example.shoesmanagement.model.Brand;
import com.example.shoesmanagement.model.enums.AppStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BrandSpecification {
    public Specification<Brand> doFilterBrand(
            String name,
            String search,
            boolean sort,
            String sortField
    ) {
        return (brandRoot,
                cq,
                cb) -> {
            cq.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.equal(brandRoot.get("name"), name));
            }
            if (search != null && !search.trim().isEmpty()) {
                String searchNew = search.trim();
                predicates.add(cb.or(
                        cb.like(brandRoot.get("name"), "%" + searchNew + "%")));

            }

            Path orderClause;
            if ("name".equals(sortField.trim())) {
                orderClause = brandRoot.get("name");
            } else {
                orderClause = brandRoot.get("createdDate");
            }

            if (sort) {
                cq.orderBy(cb.asc(orderClause));
            } else {
                cq.orderBy(cb.desc(orderClause));
            }

            return cb.and(predicates.toArray(new Predicate[]{}));

        };
    }
}
