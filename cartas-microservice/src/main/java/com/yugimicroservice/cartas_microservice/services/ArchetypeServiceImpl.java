package com.yugimicroservice.cartas_microservice.services;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.entities.dto.ArchetypeFoundResponse;
import com.yugimicroservice.cartas_microservice.entities.dto.ArchetypeResponse;
import com.yugimicroservice.cartas_microservice.repositories.ArchetypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArchetypeServiceImpl implements ArchetypeService{

    private final ArchetypeRepository archetypeRepository;

    @Override
    public ArchetypeResponse findByName(String name) {
        Archetype archetype = archetypeRepository.findByName(name).orElseThrow();
        return ArchetypeResponse.builder()
                .id(archetype.getId())
                .name(archetype.getName())
                .build();
    }

    @Transactional
    @Override
    public ArchetypeResponse save(Archetype archetype) {
        archetypeRepository.save(archetype);
        return ArchetypeResponse.builder().id(archetype.getId()).name(archetype.getName()).build();
    }

    @Override
    public List<Archetype> findAll() {
        return archetypeRepository.findAll();
    }

    @Override
    public ArchetypeFoundResponse findIfExistByName(String name) {
        Optional<Archetype> optional = archetypeRepository.findByName(name);
        return ArchetypeFoundResponse.builder()
                .found(optional.isPresent())
                .build();
    }
}
