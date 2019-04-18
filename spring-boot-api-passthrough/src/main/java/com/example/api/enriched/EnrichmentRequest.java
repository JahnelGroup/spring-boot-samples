package com.example.api.enriched;

import java.util.Objects;

public class EnrichmentRequest {

    private Integer zipCode;

    public EnrichmentRequest(){

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
        EnrichmentRequest that = (EnrichmentRequest) o;
        return Objects.equals(zipCode, that.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipCode);
    }

    @Override
    public String toString() {
        return "EnrichmentRequest{" +
                "zipCode=" + zipCode +
                '}';
    }
}
