package com.example.api.enriched;

import java.util.Optional;

public interface EnrichmentService {

    EnrichedEntity enrich(EnrichmentRequest enrichmentRequest);
    Optional<EnrichedEntity> fetch(Long id);

}
