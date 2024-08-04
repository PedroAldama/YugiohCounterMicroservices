package com.yugimicroservice.cartas_microservice.repositories;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.entities.Carta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartaRepository extends JpaRepository<Carta, Long> {
    Optional<Carta> findByName(String name);
    Optional<Carta> findByCode(String code);
    List<Carta> findAllByArchetypes(Archetype archetype);
}
