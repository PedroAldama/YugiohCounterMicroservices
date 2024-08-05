package com.yugimicroservice.situation_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SituationRequest {
    private String name;
    private String description;
    private String archetype;
}
