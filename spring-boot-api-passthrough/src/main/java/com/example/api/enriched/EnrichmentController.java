package com.example.api.enriched;

import com.example.api.enriched.scheduler.EnrichmentScheduler;
import com.example.api.enriched.service.EnrichmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnrichmentController {

    @Autowired
    private EnrichmentScheduler enrichmentScheduler;

    @Autowired
    private EnrichmentService enrichmentService;

    /**
     * Submits a unit of work to asynchronously lookup a ZipCode.
     *
     * @param enrichmentRequest
     * @return
     */
    @PostMapping("/")
    public EnrichedEntity enrich(@RequestBody EnrichmentRequest enrichmentRequest){
        return enrichmentScheduler.scheduleEnrichment(enrichmentRequest);
    }

    /**
     * Lookup the status of the asynchronous work by id.
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnrichedEntity> get(@PathVariable Long id){
        return ResponseEntity.of(enrichmentService.fetch(id));
    }

}
