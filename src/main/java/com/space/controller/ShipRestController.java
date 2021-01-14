package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/ships")
public class ShipRestController {

    @Autowired
    private ShipService shipService;
    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cosmoport?serverTimezone=UTC", "root", "root");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Optional<Ship>> getShipById(@PathVariable("id") Long shipId) {
        if (shipId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!isIdValid(shipId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Ship> ship = this.shipService.getById(shipId);
        return new ResponseEntity<>(ship, HttpStatus.OK);
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
    public ResponseEntity<Ship> deleteShip(Long id) {
        Optional<Ship> ship = this.shipService.getById(id);
        if (!ship.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!isIdValid(ship.get().getId())) {//метод get() у Optional получает значение из сущности Optional
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.shipService.delete(id);
        return new ResponseEntity<>(ship.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Ship>> getAllShips(HttpServletRequest request) {
        List<Ship> ships = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "select * from Ship where 1=1 " + shipsListFilter(request);
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                Ship ship = new Ship();
                ship.setId(result.getLong("id"));
                ship.setName(result.getString("name"));
                ship.setPlanet(result.getString("planet"));
//                ship.setShipType((ShipType) result.getObject("shipType"));
                ship.setProdDate(result.getDate("prodDate"));
                ship.setUsed(result.getBoolean("isUsed"));
                ship.setSpeed(result.getDouble("speed"));
                ship.setCrewSize(result.getInt("crewSize"));
                ship.setRating(result.getDouble("rating"));
                ships.add(ship);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ResponseEntity<>(ships, HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Integer countShips(HttpServletRequest request) {
        Integer amount = 0;
        try {
            Statement statement = connection.createStatement();
            String query = "select count(*) from Ship where 1=1 " + shipsListFilter(request);
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                amount++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return amount;
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

    /**
     * Составление sql-запроса в зависимости от пришедших параметров
     * */
    private String shipsListFilter(HttpServletRequest request) {
        StringBuilder query = new StringBuilder("");
        String name = request.getParameter("name");
        String planet = request.getParameter("planet");
        String shipType = request.getParameter("shipType");
        String after = request.getParameter("after");
        String before = request.getParameter("before");
        String isUsed = request.getParameter("isUsed");
        String minSpeed = request.getParameter("minSpeed");
        String maxSpeed = request.getParameter("maxSpeed");
        String minCrewSize = request.getParameter("minCrewSize");
        String maxCrewSize = request.getParameter("maxCrewSize");
        String minRating = request.getParameter("minRating");
        String maxRating = request.getParameter("maxRating");
        if (name != null) {
            query.append(" and name like '%" + name + "%'");
        }
        if (planet != null) {
            query.append(" and planet like '%" + planet + "%'");
        }
        if (shipType != null) {
            query.append(" and shipType = " + shipType);
        }
        if (after != null) {
            query.append(" and prodDate > " + after);
        }
        if (before != null) {
            query.append(" and prodDate < " + before);
        }
        if (isUsed != null) {
            query.append(" and isUsed = " + isUsed);
        }
        if (minSpeed != null) {
            query.append(" and speed >= " + minSpeed);
        }
        if (maxSpeed != null) {
            query.append(" and speed <= " + maxSpeed);
        }
        if (minCrewSize != null) {
            query.append(" and crew >= " + minCrewSize);
        }
        if (maxCrewSize != null) {
            query.append(" and crew <= " + maxCrewSize);
        }
        if (minRating != null) {
            query.append(" and rating >= " + minRating);
        }
        if (maxRating != null) {
            query.append(" and rating <= " + maxRating);
        }
        return query.toString().trim();
    }

}
