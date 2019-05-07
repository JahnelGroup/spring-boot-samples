package com.example.api.enriched.scheduler;

import com.example.api.enriched.EnrichedEntity;
import com.example.api.enriched.EnrichedEntityRepo;
import com.example.api.enriched.EnrichmentRequest;
import com.example.api.enriched.service.EnrichmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Schedules and transitions asynchronous work through different
 * persistent states via database queues.
 */
@Service
@Transactional
public class PersistentEnrichmentScheduler implements EnrichmentScheduler {

    private Logger logger = LoggerFactory.getLogger(PersistentEnrichmentScheduler.class);

    @Autowired
    private EnrichmentService enrichmentService;

    @Autowired
    private EnrichedEntityRepo enrichedEntityRepo;

    @Override
    public EnrichedEntity scheduleEnrichment(EnrichmentRequest enrichedEntity) {
        return enrichmentService.createRequest(enrichedEntity);
    }

    /**
     * Poll for enrichment work (SUBMITTED by submissionDateTime ASC) every 5 seconds.
     */
    @Override
    @Scheduled(fixedDelay = 5000)
    public void pollForSubmitted() {
        enrichedEntityRepo.pollForSubmitted(PageRequest.of(0, 5))
                .forEach(enrichmentService::doAsyncEnrichment);
    }


    /**
     * Poll for completed work (!SUBMITTED and responseDateTime IS NULL order by enrichmentDateTime ASC) every 10 seconds.
     */
    @Override
    @Scheduled(fixedDelay = 10000)
    public void pollForCompleted() {
        List<EnrichedEntity> completed = enrichedEntityRepo.pollForCompleted(PageRequest.of(0, 5));
        for(EnrichedEntity e : completed){
            logger.info("Enrichment completed, resp = {}", e);
            e.markEnrichmentResponseSent();
            enrichedEntityRepo.save(e);
        }
    }
}
