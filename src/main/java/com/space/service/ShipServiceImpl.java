package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    ShipRepository shipRepository;

    @Override
    public Ship getById(Long id) {//получение корабля по id
        return shipRepository.getOne(id);
    }

    @Override
    public Integer getCount() {//количество кораблей в соответствии с фильтрами
        return null;
    }

    @Override
    public void update(Ship ship) {//редактирование существующего корабля

    }

    @Override
    public void save(Ship ship) {//создание нового корабля
        shipRepository.save(ship);
    }

    @Override
    public void delete(Long id) {//удаление корабля по id
        shipRepository.deleteById(id);
    }

    @Override
    public List<Ship> getAll() {//список всех существующих кораблей
        return shipRepository.findAll();
    }

    @Override
    public List<Ship> getAllFiltered() {//отфильтрованный список кораблей
        return null;
    }

}