package com.yugimicroservice.strategy_microservice.repositories;

import com.yugimicroservice.strategy_microservice.entities.carta.Archetype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchetypeRepository extends JpaRepository<Archetype, Long> {

}
