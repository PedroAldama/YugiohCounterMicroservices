package com.yugimicroservice.strategy_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SituationFoundRequest {
    private Boolean found;
}
