package com.example.api.enriched;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class EnrichedEntity {

    public enum EnrichedEntityStatus {
        SUBMITTED,
        SUCCESS,
        FAILURE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> cityStates = new ArrayList<>();

    private Integer zipCode;

    private EnrichedEntityStatus enrichmentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnrichedEntityStatus getEnrichmentStatus() {
        return enrichmentStatus;
    }

    public void setEnrichmentStatus(EnrichedEntityStatus enrichmentStatus) {
        this.enrichmentStatus = enrichmentStatus;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrichedEntity that = (EnrichedEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(cityStates, that.cityStates) &&
                Objects.equals(zipCode, that.zipCode) &&
                enrichmentStatus == that.enrichmentStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityStates, zipCode, enrichmentStatus);
    }

    @Override
    public String toString() {
        return "EnrichedEntity{" +
                "id=" + id +
                ", cityStates=" + cityStates +
                ", enrichmentStatus=" + enrichmentStatus +
                ", zipCode=" + zipCode +
                '}';
    }


    public List<String> getCityStates() {
        return cityStates;
    }

    public void setCityStates(List<String> cityStates) {
        this.cityStates = cityStates;
    }

}
