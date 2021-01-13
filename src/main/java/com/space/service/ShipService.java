package com.space.service;

import com.space.model.Ship;
import java.util.List;

public interface ShipService {
    Ship getById(Long id);

    Integer getCount();

    void create(Ship ship);

    void delete(Long id);

    List<Ship> getAll();
}
