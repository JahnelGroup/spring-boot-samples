package com.example.api.enriched.service;

import com.example.api.enriched.EnrichedEntity;
import com.example.api.enriched.EnrichmentRequest;

import java.util.Optional;

/**
 * Service layer for performing an asynchronous enrichment and
 * fetching the results.
 */
public interface EnrichmentService {

    EnrichedEntity createRequest(EnrichmentRequest enrichmentRequest);

    EnrichedEntity doAsyncEnrichment(EnrichedEntity enrichedEntity);

    Optional<EnrichedEntity> fetch(Long id);

}
