package com.yugimicroservice.situation_microservice.repositories;

import com.yugimicroservice.situation_microservice.entities.Situation;
import com.yugimicroservice.situation_microservice.entities.TargetCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SituationRepository extends JpaRepository<Situation, Long> {
    List<Situation> findAllByArchetype(String archetype);
    Optional<Situation> findByArchetype(String archetype);
    Optional<Situation> findByName(String name);
    List<Situation> findByTargetCard(TargetCard targetCard);
}
