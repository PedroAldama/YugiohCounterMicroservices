package com.yugimicroservice.strategy_microservice.entities.dto;

import com.yugimicroservice.strategy_microservice.entities.carta.Archetype;
import com.yugimicroservice.strategy_microservice.entities.situation.SituationId;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StrategyResponse {
    private Long id;
    private String name;
    private String description;
    private String gameplay;
    private List<ArchetypeResponse> archetypeList;
    private List<SituationResponse> situationIdList;
}
