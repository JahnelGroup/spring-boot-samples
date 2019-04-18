package com.example.api.enriched;

import java.util.Optional;

public interface EnrichmentService {

    EnrichedEntity createRequest(EnrichmentRequest enrichmentRequest);

    EnrichedEntity doAsyncEnrichment(EnrichedEntity enrichedEntity);

    Optional<EnrichedEntity> fetch(Long id);

}
