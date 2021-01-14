package com.space.repository;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipRepository extends JpaRepository<Ship, Long> {

    @Query("select s from Ship s where s.name like '%:name%'")
    Ship findByName(@Param("name") String name);

    @Query("select s from Ship s where s.planet like '%:planet%'")
    Ship findByPlanet(@Param("planet") String planet);

    @Query("select s from Ship s where s.shipType = :shipType")
    Ship findByShipType(@Param("shipType")ShipType shipType);

    @Query("select s from Ship s where s.isUsed = :isUsed")
    Ship findByIsUsed(@Param("isUsed") Boolean isUsed);


}
