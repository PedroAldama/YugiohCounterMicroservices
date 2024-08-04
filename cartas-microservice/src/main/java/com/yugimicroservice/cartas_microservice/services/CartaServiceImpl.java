package com.yugimicroservice.cartas_microservice.services;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.entities.Carta;
import com.yugimicroservice.cartas_microservice.entities.dto.CartaRequest;
import com.yugimicroservice.cartas_microservice.repositories.ArchetypeRepository;
import com.yugimicroservice.cartas_microservice.repositories.CartaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartaServiceImpl implements CartaService {

    private final CartaRepository cartaRepository;
    private final ArchetypeRepository archetypeRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Carta> findAll() {
        return cartaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Carta findById(Long id) {
        Optional<Carta> carta = cartaRepository.findById(id);
        return carta.orElseThrow();
    }

    @Transactional(readOnly = true)
    @Override
    public Carta findByName(String name) {
        return cartaRepository.findByName(name).orElseThrow();
    }

    @Transactional(readOnly = true)
    @Override
    public Carta findByCode(String code) {
        return cartaRepository.findByCode(code).orElseThrow();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Carta> findByArchetype(String name) {
        Optional<Archetype> archetype = archetypeRepository.findByName(name);
        return cartaRepository.findAllByArchetypes(archetype.orElseThrow()).stream().toList();
    }

    @Transactional
    @Override
    public void save(CartaRequest carta) {
        Carta newCarta = Carta.builder()
                        .name(carta.getName())
                        .code(carta.getCode())
                        .image(carta.getImage())
                        .description(carta.getDescription())
                        .type(carta.getType())
                        .build();
        List<Archetype> archetypes = new ArrayList<>();
        archetypes.add(archetypeRepository.findByName(carta.getArchetype()).orElseThrow());
        newCarta.setArchetypes(archetypes);
        cartaRepository.save(newCarta);
    }
}
