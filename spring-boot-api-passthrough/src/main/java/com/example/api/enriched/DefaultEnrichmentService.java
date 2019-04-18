package com.example.api.enriched;

import com.example.api.smartystreets.SmartyStreetsService;
import com.example.api.smartystreets.resp.CityState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    public EnrichedEntity createRequest(EnrichmentRequest enrichmentRequest) {
        EnrichedEntity enrichedEntity = new EnrichedEntity();
        enrichedEntity.setZipCode(enrichmentRequest.getZipCode());
        enrichedEntity.setEnrichmentStatus(EnrichedEntity.EnrichedEntityStatus.SUBMITTED);
        enrichedEntityRepo.save(enrichedEntity);
        logger.info("Created request = {}", enrichmentRequest);
        return enrichedEntity;
    }

    @Async
    @Override
    public EnrichedEntity doAsyncEnrichment(EnrichedEntity enrichedEntity) {

        logger.info("Async enrichment - start = {}", enrichedEntity);

        // Pretend to take time
        simulateLatency();

        List<CityState> cityStates = smartyStreetsService.fetchCityStatesByZipCode(enrichedEntity.getZipCode());

        if( cityStates == null || cityStates.isEmpty() ){
            enrichedEntity.setEnrichmentStatus(EnrichedEntity.EnrichedEntityStatus.FAILURE);
        }else{
            enrichedEntity.setEnrichmentStatus(EnrichedEntity.EnrichedEntityStatus.SUCCESS);
            enrichedEntity.setCityStates(cityStates.stream().map(CityState::getCity).collect(Collectors.toList()));
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

    private void simulateLatency() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
