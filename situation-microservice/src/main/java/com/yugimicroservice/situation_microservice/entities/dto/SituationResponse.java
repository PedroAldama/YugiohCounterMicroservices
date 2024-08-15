package com.yugimicroservice.situation_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SituationResponse {
    private Long id;
    private String name;
    private String description;
    private String archetype;
    List<CardResponse> targetCards;
    List<CardResponse> counterCards;
}
