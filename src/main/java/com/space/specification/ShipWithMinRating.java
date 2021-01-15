package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithMinRating implements Specification<Ship> {
    private Double minRating;

    public ShipWithMinRating(Double minRating) {
        this.minRating = minRating;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (minRating == null) {
            return cb.conjunction();
        }
        return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
    }
}
