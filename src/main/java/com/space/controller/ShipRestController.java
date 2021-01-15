package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.specification.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/ships")
public class ShipRestController {

    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ship> getShipById(@PathVariable("id") Long shipId) {
        if (!isIdValid(shipId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Ship> shipOptional = this.shipService.getById(shipId);
        if (shipOptional.isPresent()) {
            Ship ship = shipOptional.get();
            return new ResponseEntity<>(ship, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        if (ship.getName() == null || ship.getPlanet() == null || ship.getName().isEmpty() || ship.getPlanet().isEmpty() ||
                ship.getShipType() == null || ship.getProdDate() == null || ship.getSpeed() == null ||
                ship.getCrewSize() == null || ship.getName().length() > 50 || ship.getPlanet().length() > 50 ||
                ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99 || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999 ||
                ship.getProdDate().getTime() < 0 || ship.getProdDate().getYear() < 2800 ||
                ship.getProdDate().getYear() > 3019) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }

        ship.setRating(ship.calculateRating());

        this.shipService.create(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Ship> updateShip(@RequestBody Ship ship) {
        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!isIdValid(ship.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ship.setRating(ship.calculateRating());

        this.shipService.create(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long id) {
        Optional<Ship> shipOptional = this.shipService.getById(id);
        if (shipOptional.isPresent()) {
            Ship ship = shipOptional.get();
            if (!isIdValid(ship.getId())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            this.shipService.delete(id);
            return new ResponseEntity<>(ship, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Ship>> getAllShips(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "planet", required = false) String planet,
                                            @RequestParam(value = "shipType", required = false) ShipType shipType, @RequestParam(value = "after", required = false) Long after,
                                            @RequestParam(value = "before", required = false) Long before, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                            @RequestParam(value = "minSpeed", required = false) Double minSpeed, @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize, @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                            @RequestParam(value = "minRating", required = false) Double minRating, @RequestParam(value = "maxRating", required = false) Double maxRating,
                                            @RequestParam(value = "order", required = false) ShipOrder order, @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
//        List<Ship> ships = this.shipService.getAll();
        List<Ship> ships;

        //если эти параметры не указаны
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 3;
        }

        Pageable pageable; //пагинация + сортировка
        if (order == ShipOrder.ID) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        } else if (order == ShipOrder.SPEED) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("speed"));
        } else if (order == ShipOrder.DATE) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("prodDate"));
        } else if (order == ShipOrder.RATING) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("rating"));
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }

        Specification<Ship> spec = makeSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        ships = this.shipService.getAll(spec, pageable);

        return new ResponseEntity<>(ships, HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Integer countShips(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "planet", required = false) String planet,
                              @RequestParam(value = "shipType", required = false) ShipType shipType, @RequestParam(value = "after", required = false) Long after,
                              @RequestParam(value = "before", required = false) Long before, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                              @RequestParam(value = "minSpeed", required = false) Double minSpeed, @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                              @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize, @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                              @RequestParam(value = "minRating", required = false) Double minRating, @RequestParam(value = "maxRating", required = false) Double maxRating) {
        Specification<Ship> spec = makeSpecification(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        List<Ship> ships = this.shipService.getAll(spec);
        return ships.size();
    }

    /**
     * Проверка числа на валидность (является числом, целое, положительное)
     * */
    private boolean isIdValid(Long id) {
        try {
            String shipId = id.toString();
            Long.parseLong(shipId);//проверка, является ли переданный аргумент числом
        } catch (NumberFormatException e) {
            return false;
        }

        Long roundedId = (long) Math.floor(id);//округляем до ближайшего целого вниз.
        //Если число равно shipId значит shipId - целое число
        return id.equals(roundedId) && id >= 0;
    }

    private Specification<Ship> makeSpecification(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "planet", required = false) String planet,
                                                  @RequestParam(value = "shipType", required = false) ShipType shipType, @RequestParam(value = "after", required = false) Long after,
                                                  @RequestParam(value = "before", required = false) Long before, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                  @RequestParam(value = "minSpeed", required = false) Double minSpeed, @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                                  @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize, @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                                  @RequestParam(value = "minRating", required = false) Double minRating, @RequestParam(value = "maxRating", required = false) Double maxRating) {
        return Specifications.where(new ShipWithName(name)).and(new ShipWithPlanet(planet))
                .and(new ShipWithShipType(shipType)).and(new ShipWithAfter(after)).and(new ShipWithBefore(before))
                .and(new ShipWithIsUsed(isUsed)).and(new ShipWithMinSpeed(minSpeed)).and(new ShipWithMaxSpeed(maxSpeed))
                .and(new ShipWithMinCrewSize(minCrewSize)).and(new ShipWithMaxCrewSize(maxCrewSize)).and(new ShipWithMinRating(minRating))
                .and(new ShipWithMaxRating(maxRating));
    }

}
