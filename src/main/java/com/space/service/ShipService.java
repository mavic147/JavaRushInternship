package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ShipService {
    Optional<Ship> getById(Long id);

    Integer getCount();

    void create(Ship ship);

    void delete(Long id);

    List<Ship> getAll(Specification<Ship> specification, Pageable pageable);

    List<Ship> getAll(Specification<Ship> specification);
}
