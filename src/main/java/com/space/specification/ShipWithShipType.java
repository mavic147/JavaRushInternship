package com.space.specification;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ShipWithShipType implements Specification<Ship> {
    private ShipType shipType;

    public ShipWithShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        if (shipType == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.equal(root.get("shipType"), this.shipType);
    }
}
