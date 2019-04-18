package com.example.api.smartystreets;

import com.example.api.smartystreets.resp.CityState;
import com.example.api.enriched.EnrichmentRequest;

import java.util.List;

public interface SmartyStreetsService {

    List<CityState> fetchCityStates(EnrichmentRequest enrichmentRequest);

}
