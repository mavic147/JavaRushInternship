package com.space.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithPlanet implements Specification<Ship> {
    private String planet;

    public ShipWithPlanet(String planet) {
        this.planet = planet;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (planet == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.like(root.get("planet"), planet);
    }
}
