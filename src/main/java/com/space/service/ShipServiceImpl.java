package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public Optional<Ship> getById(Long id) {//получение корабля по id
        return shipRepository.findById(id);
    }

    @Override
    public Integer getCount() {//количество кораблей в соответствии с фильтрами
        return 0;
    }

    @Override
    public void create(Ship ship) {//создание нового корабля
        shipRepository.save(ship);
    }

    @Override
    public void delete(Long id) {//удаление корабля по id
        shipRepository.deleteById(id);
    }

    @Override
    public List<Ship> getAll(Specification<Ship> specification, Pageable pageable) {
        return shipRepository.findAll(specification);
    }

    @Override
    public List<Ship> getAll(Specification<Ship> specification) {
        return shipRepository.findAll(specification);
    }
}
