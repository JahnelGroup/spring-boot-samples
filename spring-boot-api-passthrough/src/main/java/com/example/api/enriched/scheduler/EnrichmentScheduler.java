package com.example.api.enriched.scheduler;

import com.example.api.enriched.EnrichedEntity;
import com.example.api.enriched.EnrichmentRequest;

/**
 * Schedules asynchronous enrichment work.
 */
public interface EnrichmentScheduler {

    EnrichedEntity scheduleEnrichment(EnrichmentRequest enrichedEntity);

    /**
     * EnrichedEntityStatus.SUBMITTED
     */
    void pollForSubmitted();

    /**
     * EnrichedEntityStatus.SUCCESS
     * EnrichedEntityStatus.FAILURE
     */
    void pollForCompleted();

}
