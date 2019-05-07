package com.example.api.enriched.service;

import com.example.api.enriched.EnrichedEntity;
import com.example.api.enriched.EnrichedEntityRepo;
import com.example.api.enriched.EnrichmentRequest;
import com.example.api.smartystreets.SmartyStreetsService;
import com.example.api.smartystreets.resp.CityState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of EnrichmentService.
 */
@Service
public class DefaultEnrichmentService implements EnrichmentService {

    private Logger logger = LoggerFactory.getLogger(DefaultEnrichmentService.class);

    @Autowired
    private EnrichedEntityRepo enrichedEntityRepo;

    @Autowired
    private SmartyStreetsService smartyStreetsService;

    @Override
    public EnrichedEntity createRequest(EnrichmentRequest enrichmentRequest) {
        EnrichedEntity enrichedEntity = EnrichedEntity.fromEnrichmentRequest(enrichmentRequest);
        enrichedEntityRepo.save(enrichedEntity);
        logger.info("Created request = {}", enrichmentRequest);
        return enrichedEntity;
    }

    @Async
    @Override
    public EnrichedEntity doAsyncEnrichment(EnrichedEntity enrichedEntity) {

        logger.info("Async enrichment - start = {}", enrichedEntity);

        List<CityState> cityStates = null;

        try{
            cityStates = smartyStreetsService.fetchCityStatesByZipCode(enrichedEntity.getZipCode());
        }catch(Exception e){
            logger.error("Failed to enrich from SmartyStreets", e);
        }

        if( cityStates == null || cityStates.isEmpty() ){
            enrichedEntity.markEnrichmentFailed();
        }else{
            enrichedEntity.markEnrichmentSuccess(cityStates.stream().map(CityState::getCity).collect(Collectors.toList()));
        }

        logger.info("Async enrichment - done = {}", enrichedEntity);

        return enrichedEntityRepo.save(enrichedEntity);
    }

    @Override
    public Optional<EnrichedEntity> fetch(Long id) {
        Optional<EnrichedEntity> enrichedEntity = enrichedEntityRepo.findById(id);
        logger.info("Fetch for {} resulted in {}", id, enrichedEntity);
        return enrichedEntity;
    }

}
