package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class ShipWithAfter implements Specification<Ship> {
    private Long after;

    public ShipWithAfter(Long after) {
        this.after = after;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (after == null) {
            return cb.isTrue(cb.literal(true));
        }
        Date dateAfter = new Date(after);
        return cb.greaterThan(root.get("prodDate"), dateAfter);
    }
}
