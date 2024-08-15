package com.yugimicroservice.strategy_microservice.repositories;

import com.yugimicroservice.strategy_microservice.entities.situation.SituationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SituationRepository extends JpaRepository<SituationId, Long> {
    Optional<SituationId> findById(Long id);
}
