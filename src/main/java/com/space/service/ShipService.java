package com.space.service;

import com.space.model.Ship;
import java.util.List;

public interface ShipService {
    Ship getById(Long id);

    Integer getCount();

    void update(Ship ship);

    void save(Ship ship);

    void delete(Long id);

    List<Ship> getAll();

    List<Ship> getAllFiltered();
}
