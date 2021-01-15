package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithBefore implements Specification<Ship> {
    private Long before;

    public ShipWithBefore(Long before) {
        this.before = before;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (before == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.lessThan(root.get("prodDate"), before);
    }
}
