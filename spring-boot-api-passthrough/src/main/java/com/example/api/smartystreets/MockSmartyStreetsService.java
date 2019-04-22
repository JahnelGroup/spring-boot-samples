package com.example.api.smartystreets;

import com.example.api.smartystreets.resp.CityState;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Profile("mock")
@Component
public class MockSmartyStreetsService implements SmartyStreetsService {

    private Logger logger = LoggerFactory.getLogger(MockSmartyStreetsService.class);

    @Value("classpath:json/data.json")
    Resource resourceFile;

    @Autowired
    ObjectMapper objectMapper;

    private Map<Integer, List<CityState>> mockData = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        JsonNode jsonNode = objectMapper.readTree(resourceFile.getFile());
        jsonNode.fields();

        // for each zip code
        for (Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields(); it.hasNext(); ) {
            List<CityState> cityStates = new ArrayList<>();

            // read the city state array
            Map.Entry<String, JsonNode> node = it.next();
            for(JsonNode cityState: node.getValue()){
                cityStates.add(new CityState(cityState.asText()));
            }

            // save it to memory
            if( !cityStates.isEmpty() ){
                mockData.put(Integer.parseInt(node.getKey()), cityStates);
            }
        }
    }

    @Override
    public List<CityState> fetchCityStatesByZipCode(Integer zipCode) {
        List<CityState> cityStates = mockData.get(zipCode);
        logger.info("Fetched mocked city_states = {}", cityStates);
        return cityStates;
    }
}
