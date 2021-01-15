package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithMaxCrewSize implements Specification<Ship> {
    private Integer maxCrewSize;

    public ShipWithMaxCrewSize(Integer maxCrewSize) {
        this.maxCrewSize = maxCrewSize;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (maxCrewSize == null) {
            return cb.conjunction();
        }
        return cb.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
    }
}
