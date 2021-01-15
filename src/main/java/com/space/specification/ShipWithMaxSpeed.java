package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithMaxSpeed implements Specification<Ship> {
    private Double maxSpeed;

    public ShipWithMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (maxSpeed == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.lessThanOrEqualTo(root.get("speed"), maxSpeed);
    }
}
