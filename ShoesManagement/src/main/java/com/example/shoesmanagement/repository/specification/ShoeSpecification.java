package com.example.shoesmanagement.repository.specification;

import com.example.shoesmanagement.model.Shoe;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
@Component
public class ShoeSpecification {
    public Specification<Shoe> doFilterShoe(
            String search,
            List<Long> idBrands,
            double minPrice,
            double maxPrice,
            boolean sort,
            String sortField
    ) {
        return (brandRoot,
                cq,
                cb) -> {
            cq.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.trim().isEmpty()) {
                String searchNew = search.trim();
                predicates.add(cb.like(brandRoot.get("name"), "%" + searchNew + "%"));

            }
            if (idBrands != null) {
//                idBrands.stream().map(x -> cb.equal(brandRoot.get("idBrand"), x)).toArray();
//                Predicate[] idBrandPredicates = new Predicate[idBrands.size()];
//                for (int i = 0; i < idBrands.size(); i++) {
//                    idBrandPredicates[i] = cb.equal(brandRoot.get("idBrand"), idBrands.get(i));
//                }
                predicates.add(cb.or(idBrands.stream().map(x -> cb.equal(brandRoot.get("idBrand"), x)).toArray(Predicate[]::new)));
            }
            if (minPrice < maxPrice && minPrice >= 0 && maxPrice >= 0){
                predicates.add(cb.between(brandRoot.get("discount"), minPrice, maxPrice));
            }

            Path orderClause;
            if (sortField != null){
                if ("name".equals(sortField.trim())) {
                    orderClause = brandRoot.get("name");
                } else if ("discountPrice".equals(sortField.trim())) {
                    orderClause = brandRoot.get("discount");
                } else {
                    orderClause = brandRoot.get("createdDate");
                }

                if (sort) {
                    cq.orderBy(cb.asc(orderClause));
                } else {
                    cq.orderBy(cb.desc(orderClause));
                }
            }

            return cb.and(predicates.toArray(new Predicate[]{}));

        };
    }
}
