package com.yugimicroservice.strategy_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StrategyRequest {
    private String name;
    private String description;
    private String gameplay;
    private List<Long> archetypesId;
    private List<Long> situationsId;
}
