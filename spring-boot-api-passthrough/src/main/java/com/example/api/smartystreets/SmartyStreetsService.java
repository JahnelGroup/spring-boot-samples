package com.example.api.smartystreets;

import com.example.api.smartystreets.resp.CityState;

import java.util.List;

public interface SmartyStreetsService {

    List<CityState> fetchCityStatesByZipCode(Integer zipCode);

}
