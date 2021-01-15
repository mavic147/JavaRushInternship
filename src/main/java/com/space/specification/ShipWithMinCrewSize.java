package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithMinCrewSize implements Specification<Ship> {
    private Integer minCrewSize;

    public ShipWithMinCrewSize(Integer minCrewSize) {
        this.minCrewSize = minCrewSize;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (minCrewSize == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
    }
}
