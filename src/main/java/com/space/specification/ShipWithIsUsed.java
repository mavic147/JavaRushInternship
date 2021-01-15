package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithIsUsed implements Specification<Ship> {
    private Boolean isUsed;

    public ShipWithIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (isUsed == null) {
            return cb.conjunction();
        }
        return cb.equal(root.get("isUsed"), isUsed);
    }
}
