package com.space.model;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Simple object representing a ship.
 */
@Entity
@Table(name = "ship")
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "planet")
    private String planet;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipType")
    private ShipType shipType;

    @Column(name = "prodDate")
    private Date prodDate;

    @Column(name = "isUsed")
    private Boolean isUsed;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * Рассчет рейтинга корабля. Используется в методах createShip и updateShip в ShipRestController *3019 - текущий год
     */
    public Double calculateRating() {
        Double coefficient = this.isUsed ? 0.5 : 1.0;
        DecimalFormat df = new DecimalFormat("#.##");
        try {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(this.getProdDate().getTime());
            Double rating = (80 * this.getSpeed() * coefficient) / (3019 - date.get(Calendar.YEAR) + 1);
            return Double.parseDouble(df.format(rating).replace(",", "."));
        } catch (ArithmeticException e) {
            return 0.00;
        }
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType='" + shipType + '\'' +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
