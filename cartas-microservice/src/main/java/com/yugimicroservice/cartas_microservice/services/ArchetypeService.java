package com.yugimicroservice.cartas_microservice.services;


import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.entities.dto.ArchetypeFoundResponse;
import com.yugimicroservice.cartas_microservice.entities.dto.ArchetypeResponse;

import java.util.List;

public interface ArchetypeService {
    ArchetypeResponse findByName(String name);
    void save(Archetype archetype);
    List<Archetype> findAll();
    ArchetypeFoundResponse findIfExistByName(String name);
}
