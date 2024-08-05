package com.yugimicroservice.situation_microservice.servicies;

import com.yugimicroservice.situation_microservice.entities.Situation;
import com.yugimicroservice.situation_microservice.entities.dto.ArchetypeFound;
import com.yugimicroservice.situation_microservice.entities.dto.CartaRequest;
import com.yugimicroservice.situation_microservice.entities.dto.SituationRequest;

import java.util.List;

public interface SituationService {
    void save(Situation situation);
    Situation getSituationByArchetype(String archetype);
    List<Situation> getAllSituationsByArchetype(String archetype);
    String createSituation(SituationRequest situationRequest);
    void addTargetCard(String name, CartaRequest target);
    void addCounterCard(String name, CartaRequest counter);
    ArchetypeFound getArchetype(String name);
}
