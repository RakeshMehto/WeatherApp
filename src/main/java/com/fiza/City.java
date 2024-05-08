package com.fiza;

import java.util.Map;

public class City {
    private Integer id;
    private String name;
    private String country;
    private Map<String, Double> coord;

    public City(Integer id, String name, String country, Map<String, Double> coord) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.coord = coord;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Map<String, Double> getCoord() {
        return coord;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", coord=" + coord +
                '}';
    }
}
