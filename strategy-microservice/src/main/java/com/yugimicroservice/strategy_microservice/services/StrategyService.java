package com.yugimicroservice.strategy_microservice.services;


import com.yugimicroservice.strategy_microservice.entities.Strategy;
import com.yugimicroservice.strategy_microservice.entities.dto.StrategyAddRequest;
import com.yugimicroservice.strategy_microservice.entities.dto.StrategyRequest;
import com.yugimicroservice.strategy_microservice.entities.dto.StrategyResponse;

import java.util.List;

public interface StrategyService{
    StrategyResponse save(StrategyRequest strategy);
    List<StrategyResponse> getAllStrategies();
    StrategyResponse getStrategyById(Long id);
    StrategyResponse getStrategyByName(String name);
    List<StrategyResponse> getAllByArchetype(String name);
    List<String> addSituation(StrategyAddRequest request);
    List<String> addArchetype(StrategyAddRequest request);
    String removeSituation(StrategyAddRequest request);
    String removeArchetype(StrategyAddRequest request);
}
