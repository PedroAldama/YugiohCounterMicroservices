package com.yugimicroservice.situation_microservice.servicies;

import com.yugimicroservice.situation_microservice.entities.Situation;
import com.yugimicroservice.situation_microservice.entities.dto.*;

import java.util.List;

public interface SituationService {
    void save(Situation situation);
    SituationResponse getById(Long id);
    List<SituationResponse> getAllSituationsByArchetype(String archetype);
    String createSituation(SituationRequest situationRequest);
    String addTargetCard(String name, RequestCardName cardName);
    String addCounterCard(String name, RequestCardName cardRequest);
    ArchetypeFound getArchetype(String name);
    List<SituationResponse> getSituationForCard(String card);
    SituationFound getIfExists(Long id);
}
