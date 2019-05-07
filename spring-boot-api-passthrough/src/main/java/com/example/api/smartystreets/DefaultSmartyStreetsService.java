package com.example.api.smartystreets;

import com.example.api.smartystreets.resp.CityState;
import com.example.api.smartystreets.resp.SmartyStreetsResponse;
import com.example.api.enriched.EnrichmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Profile("!mock")
@Component
public class DefaultSmartyStreetsService implements SmartyStreetsService {

    private Logger logger = LoggerFactory.getLogger(DefaultSmartyStreetsService.class);

    private final String BASE_URL;
    private final String ZIPCODE_URL = "https://us-zipcode.api.smartystreets.com/lookup";
    private final String AUTH_ID     = "b1a31de7-8fc1-29d6-f75c-557d2480aa4d";
    private final String AUTH_TOKEN  = "Rza2L4GI3ZsizyVZacRh";

    private RestTemplate restTemplate = new RestTemplate();

    public DefaultSmartyStreetsService(){
        BASE_URL = String.format("%s?auth-id=%s&auth-token=%s", ZIPCODE_URL, AUTH_ID, AUTH_TOKEN);
    }

    @Override
    public List<CityState> fetchCityStatesByZipCode(Integer zipCode) {
        // TODO: use urivariables for expansion instead of string concat
        ResponseEntity<List<SmartyStreetsResponse>> resp = restTemplate.exchange(
                BASE_URL + "&zipcode=" + zipCode,
                HttpMethod.GET,
                null, // requestEntity
                new ParameterizedTypeReference<List<SmartyStreetsResponse>>() {
                });

        if( resp.getStatusCode() != HttpStatus.OK || resp.getBody() == null || resp.getBody().isEmpty() ){
            // TODO: save error on async payload instead of throwing
            throw new RuntimeException("Unable to retrieve data from SmartyStreets");
        }

        // Pretend to take time
        simulateLatency();

        List<CityState> cityStates = resp.getBody().get(0).getCityStates();
        logger.info("Fetched city_states = {}", cityStates);
        return cityStates;
    }

    /**
     * SmartyStreets is fast so to simulate latency we can just sleep.
     */
    private void simulateLatency() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}