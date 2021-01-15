package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithMinSpeed implements Specification<Ship> {
    private Double minSpeed;

    public ShipWithMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (minSpeed == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.greaterThanOrEqualTo(root.get("speed"), minSpeed);
    }
}
