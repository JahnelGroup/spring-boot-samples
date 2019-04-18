package com.example.api.enriched;

import com.example.api.smartystreets.SmartyStreetsService;
import com.example.api.smartystreets.resp.CityState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DefaultEnrichmentService implements EnrichmentService {

    private Logger logger = LoggerFactory.getLogger(DefaultEnrichmentService.class);

    @Autowired
    private EnrichedEntityRepo enrichedEntityRepo;

    @Autowired
    private SmartyStreetsService smartyStreetsService;

    @Override
    public EnrichedEntity enrich(EnrichmentRequest enrichmentRequest) {
        List<CityState> cityStates = smartyStreetsService.fetchCityStates(enrichmentRequest);
        EnrichedEntity enrichedEntity = new EnrichedEntity();
        enrichedEntity.setCityStates(cityStates.stream().map(CityState::getCity).collect(Collectors.toList()));
        return enrichedEntityRepo.save(enrichedEntity);
    }

    @Override
    public Optional<EnrichedEntity> fetch(Long id) {
        Optional<EnrichedEntity> enrichedEntity = enrichedEntityRepo.findById(id);
        return enrichedEntity;
    }
}
