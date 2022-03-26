package com.querotattoo.entities.dto;

import com.querotattoo.entities.TattooEstimate;
import lombok.Data;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
public class TattooEstimateReadDTO {

    private String artist;
    private String customer;
    private String minValue;
    private String maxValue;
    private String idea;
    private String placeOfBody;
    private String status;

    @ElementCollection
    private List<String> tattooReferences;

    public TattooEstimateReadDTO() {

    }

    public TattooEstimateReadDTO(TattooEstimate estimate) {
        this.artist = estimate.getArtist().getName();
        this.customer = estimate.getCustomer().getName();
        this.minValue = estimate.getMinValue() != null ? estimate.getMinValue().toString() : "";
        this.maxValue = estimate.getMaxValue() != null ? estimate.getMaxValue().toString() : "";
        this.idea = estimate.getIdea();
        this.placeOfBody = estimate.getPlaceOfBody();
        this.tattooReferences = estimate.getTattooReferences();
        this.status=estimate.getStatus();
    }
}
