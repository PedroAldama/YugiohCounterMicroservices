package com.yugimicroservice.cartas_microservice.repositories;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchetypeRepository extends JpaRepository<Archetype, Long> {
    Optional<Archetype> findByName(String name);
}
