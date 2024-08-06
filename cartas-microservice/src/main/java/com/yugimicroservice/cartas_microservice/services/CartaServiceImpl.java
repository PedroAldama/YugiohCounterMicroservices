package com.yugimicroservice.cartas_microservice.services;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.entities.Carta;
import com.yugimicroservice.cartas_microservice.entities.dto.CardFoundResponse;
import com.yugimicroservice.cartas_microservice.entities.dto.CardResponse;
import com.yugimicroservice.cartas_microservice.entities.dto.CartaArchetype;
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
    public List<CardResponse> findAll() {
        List<Carta> cards = cartaRepository.findAll();

        return cards.stream().map(carta -> CardResponse.builder()
           .name(carta.getName())
           .description(carta.getDescription())
           .type(carta.getType())
           .code(carta.getCode())
           .image(carta.getImage())
           .build()
        ).toList();
    }


    @Transactional(readOnly = true)
    @Override
    public CardResponse findByName(String name) {
        Optional<Carta> optionalCarta = cartaRepository.findByName(name);
        return optionalToResponse(optionalCarta);
    }

    @Transactional(readOnly = true)
    @Override
    public CardResponse findByCode(String code) {
        Optional<Carta> optionalCarta = cartaRepository.findByCode(code);
        return optionalToResponse(optionalCarta);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CardResponse> findByArchetype(String name) {
        Optional<Archetype> archetype = archetypeRepository.findByName(name);

        return archetype.map(value -> cartaRepository.findAllByArchetypes(value)
                .stream()
                .map(this::cardToResponse)
                .toList()).orElse(new ArrayList<>());
    }

    @Transactional(readOnly = true)
    @Override
    public CardFoundResponse cardFound(String name, String code) {
        if(cartaRepository.findByName(name).isEmpty() || cartaRepository.findByCode(code).isEmpty()){
            return CardFoundResponse.builder().found(Boolean.FALSE).build();
        }
        return CardFoundResponse.builder().found(Boolean.TRUE).build();
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
        Optional<Archetype> OptionalArchetype = archetypeRepository.findByName(carta.getArchetype());

        if(OptionalArchetype.isPresent()){
            archetypes.add(OptionalArchetype.get());
        }else{
            Archetype newArchetype = Archetype.builder().name(carta.getArchetype()).build();
            archetypeRepository.save(newArchetype);
            archetypes.add(newArchetype);
        }
        newCarta.setArchetypes(archetypes);
        cartaRepository.save(newCarta);
    }

    @Override
    public String addArchetype(CartaArchetype cartaArchetype) {
        Optional<Carta> optionalCarta = cartaRepository.findByName(cartaArchetype.getName());
        Optional<Archetype> optionalArchetype = archetypeRepository.findByName(cartaArchetype.getArchetype());

        if(optionalCarta.isPresent() && optionalArchetype.isPresent()){
            List<Archetype> archetypeList = optionalCarta.get().getArchetypes();
            archetypeList.add(optionalArchetype.get());
            optionalCarta.get().setArchetypes(archetypeList);
            cartaRepository.save(optionalCarta.get());
            return "Archetype "
                    + optionalArchetype.get().getName()
                    + " added to " +
                    optionalCarta.get().getName();
        }

        return "Archetype or Cards not found";
    }

    private CardResponse optionalToResponse(Optional<Carta> cardOptional){
        if(cardOptional.isPresent()){
            Carta card = cardOptional.get();
            return CardResponse.builder()
                    .name(card.getName())
                    .description(card.getDescription())
                    .image(card.getImage())
                    .code(card.getCode())
                    .type(card.getType())
                    .build();
        }
        return CardResponse.builder().build();
    }
    private CardResponse cardToResponse(Carta card){
            return CardResponse.builder()
                    .name(card.getName())
                    .description(card.getDescription())
                    .image(card.getImage())
                    .code(card.getCode())
                    .type(card.getType())
                    .build();
    }
}
