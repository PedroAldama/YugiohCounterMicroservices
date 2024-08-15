package com.yugimicroservice.strategy_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StrategyAddRequest {
    String strategy;
    List<Long> idList;
}
