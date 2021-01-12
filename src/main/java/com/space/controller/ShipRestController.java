package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/ships")
public class ShipRestController {

    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ship> getShipById(@PathVariable("id") Long shipId) {
        if (shipId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            String id = shipId.toString();
            Long.parseLong(id);//проверка, является ли переданный аргумент числом
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long roundedId = (long) Math.floor(shipId);//округляем до ближайшего целого вниз.
        if (!shipId.equals(roundedId) || shipId < 0) {//Если число равно shipId значит shipId - целое число
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Ship ship = this.shipService.getById(shipId);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Ship> createShip(@RequestBody @Validated Ship ship) {
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

//    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
//    public ResponseEntity<Ship> updateShip(Ship ship) {
//        if (ship == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//
//
//    }


}
