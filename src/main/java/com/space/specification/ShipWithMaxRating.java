package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithMaxRating implements Specification<Ship> {
    private Double maxRating;

    public ShipWithMaxRating(Double maxRating) {
        this.maxRating = maxRating;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (maxRating == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.lessThanOrEqualTo(root.get("rating"), maxRating);
    }
}
