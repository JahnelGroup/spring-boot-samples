package com.example.api.enriched;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.api.enriched.EnrichedEntity.EnrichedEntityStatus.FAILURE;
import static com.example.api.enriched.EnrichedEntity.EnrichedEntityStatus.SUCCESS;

/**
 * Primary data model that represents a piece of asynchronous work with the requested data,
 * calculated data (fetch data from SmartyStreets), in addition to timestamp and statues.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
public class EnrichedEntity {

    public enum EnrichedEntityStatus {
        SUBMITTED,
        SUCCESS,
        FAILURE
    }

    //
    // Properties
    //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> cityStates = new ArrayList<>();

    private Integer zipCode;

    @Enumerated(EnumType.STRING)
    private EnrichedEntityStatus enrichmentStatus;

    private Instant submissionDateTime = null;
    private Instant enrichmentDateTime = null;
    private Instant responseDateTime = null;

    //
    // Getters and Setters
    //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getCityStates() {
        return cityStates;
    }

    public void setCityStates(List<String> cityStates) {
        this.cityStates = cityStates;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public EnrichedEntityStatus getEnrichmentStatus() {
        return enrichmentStatus;
    }

    public void setEnrichmentStatus(EnrichedEntityStatus enrichmentStatus) {
        this.enrichmentStatus = enrichmentStatus;
    }

    public Instant getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(Instant submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public Instant getEnrichmentDateTime() {
        return enrichmentDateTime;
    }

    public void setEnrichmentDateTime(Instant enrichmentDateTime) {
        this.enrichmentDateTime = enrichmentDateTime;
    }

    public Instant getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(Instant responseDateTime) {
        this.responseDateTime = responseDateTime;
    }


    //
    // Factory Methods
    //

    public static EnrichedEntity fromEnrichmentRequest(EnrichmentRequest enrichmentRequest){
        EnrichedEntity enrichedEntity = new EnrichedEntity();
        enrichedEntity.setZipCode(enrichmentRequest.getZipCode());
        enrichedEntity.setEnrichmentStatus(EnrichedEntity.EnrichedEntityStatus.SUBMITTED);
        enrichedEntity.setSubmissionDateTime(Instant.now());
        return enrichedEntity;
    }

    // Mutable methods

    public EnrichedEntity markEnrichmentFailed(){
        this.setEnrichmentStatus(FAILURE);
        this.setEnrichmentDateTime(Instant.now());
        return this;
    }

    public EnrichedEntity markEnrichmentSuccess(List<String> cityStates){
        this.setEnrichmentStatus(SUCCESS);
        this.setCityStates(cityStates);
        this.setEnrichmentDateTime(Instant.now());
        return this;
    }

    public EnrichedEntity markEnrichmentResponseSent(){
        this.setResponseDateTime(Instant.now());
        return this;
    }

    //
    // HashCode, Equals, ToString
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrichedEntity that = (EnrichedEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(cityStates, that.cityStates) &&
                Objects.equals(zipCode, that.zipCode) &&
                enrichmentStatus == that.enrichmentStatus &&
                Objects.equals(submissionDateTime, that.submissionDateTime) &&
                Objects.equals(enrichmentDateTime, that.enrichmentDateTime) &&
                Objects.equals(responseDateTime, that.responseDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityStates, zipCode, enrichmentStatus, submissionDateTime, enrichmentDateTime, responseDateTime);
    }

    @Override
    public String toString() {
        return "EnrichedEntity{" +
                "id=" + id +
                ", zipCode=" + zipCode +
                ", enrichmentStatus=" + enrichmentStatus +
                ", submissionDateTime=" + submissionDateTime +
                ", enrichmentDateTime=" + enrichmentDateTime +
                ", responseDateTime=" + responseDateTime +
                '}';
    }
}
