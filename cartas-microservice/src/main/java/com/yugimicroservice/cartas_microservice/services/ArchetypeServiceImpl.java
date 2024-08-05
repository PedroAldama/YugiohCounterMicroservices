package com.yugimicroservice.cartas_microservice.services;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.repositories.ArchetypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArchetypeServiceImpl implements ArchetypeService{

    private final ArchetypeRepository archetypeRepository;

    @Override
    public Archetype findByName(String name) {
        return archetypeRepository.findByName(name).orElseThrow();
    }

    @Override
    public void save(Archetype archetype) {
        archetypeRepository.save(archetype);
    }

    @Override
    public List<Archetype> findAll() {
        return archetypeRepository.findAll();
    }

    @Override
    public Boolean findIfExistByName(String name) {
        Optional<Archetype> optional = archetypeRepository.findByName(name);
        return optional.isPresent();
    }
}
