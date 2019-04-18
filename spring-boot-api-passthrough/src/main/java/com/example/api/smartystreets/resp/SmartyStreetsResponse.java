package com.example.api.smartystreets.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class SmartyStreetsResponse {

    @JsonProperty("city_states")
    private List<CityState> cityStates;

    public List<CityState> getCityStates() {
        return cityStates;
    }

    public void setCityStates(List<CityState> cityStates) {
        this.cityStates = cityStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartyStreetsResponse that = (SmartyStreetsResponse) o;
        return Objects.equals(cityStates, that.cityStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityStates);
    }
}
