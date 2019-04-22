package com.example.api.smartystreets.resp;

import java.util.Objects;

public class CityState {

    private String city;

    // default constructor needed for JPA
    public CityState(){}

    public CityState(String city){
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityState that = (CityState) o;
        return Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city);
    }

    @Override
    public String toString() {
        return "CityState{" +
                "city='" + city + '\'' +
                '}';
    }
}
