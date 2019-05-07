package com.example.api.enriched;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnrichedEntityRepo extends CrudRepository<EnrichedEntity, Long> {

    @Query("select e from EnrichedEntity e where e.enrichmentStatus = 'SUBMITTED' " +
            "order by e.submissionDateTime ASC")
    List<EnrichedEntity> pollForSubmitted(Pageable pageable);

    @Query("select e from EnrichedEntity e where NOT e.enrichmentStatus = 'SUBMITTED' AND responseDateTime IS NULL " +
            "order by e.enrichmentDateTime ASC")
    List<EnrichedEntity> pollForCompleted(Pageable pageable);

}
