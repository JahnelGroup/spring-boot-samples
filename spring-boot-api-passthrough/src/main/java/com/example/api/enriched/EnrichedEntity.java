package com.example.api.enriched;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class EnrichedEntity {

    public static enum EnrichedEntityStatus {
        SUBMITTED,
        COMPLETE,
        FAILURE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> cityStates = new ArrayList<>();

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

    @Override
    public String toString() {
        return "EnrichedEntity{" +
                "id=" + id +
                ", cityStates=" + cityStates +
                ", enrichmentStatus=" + enrichmentStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrichedEntity that = (EnrichedEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(cityStates, that.cityStates) &&
                enrichmentStatus == that.enrichmentStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityStates, enrichmentStatus);
    }

    public void setEnrichmentStatus(EnrichedEntityStatus enrichmentStatus) {
        this.enrichmentStatus = enrichmentStatus;
    }

    public List<String> getCityStates() {
        return cityStates;
    }

    public void setCityStates(List<String> cityStates) {
        this.cityStates = cityStates;
    }

}
