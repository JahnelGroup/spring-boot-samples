package com.example.api.enriched;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnrichmentController {

    private Logger logger = LoggerFactory.getLogger(EnrichmentController.class);

    @Autowired
    private EnrichmentService enrichmentService;

    @PostMapping("/")
    public EnrichedEntity enrich(@RequestBody EnrichmentRequest enrichmentRequest){
        return enrichmentService.enrich(enrichmentRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrichedEntity> get(@PathVariable Long id){
        return ResponseEntity.of(enrichmentService.fetch(id));
    }

}
