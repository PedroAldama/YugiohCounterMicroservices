package com.yugimicroservice.cartas_microservice.services;


import com.yugimicroservice.cartas_microservice.entities.Archetype;

import java.util.List;

public interface ArchetypeService {
    Archetype findByName(String name);
    void save(Archetype archetype);
    List<Archetype> findAll();
    Boolean findIfExistByName(String name);
}
