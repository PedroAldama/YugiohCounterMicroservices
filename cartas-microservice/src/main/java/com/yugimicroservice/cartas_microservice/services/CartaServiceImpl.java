package com.yugimicroservice.cartas_microservice.services;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yugimicroservice.cartas_microservice.entities.Carta;
import com.yugimicroservice.cartas_microservice.entities.dto.*;
import com.yugimicroservice.cartas_microservice.repositories.ArchetypeRepository;
import com.yugimicroservice.cartas_microservice.repositories.CartaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartaServiceImpl implements CartaService {

    private final CartaRepository cartaRepository;
    private final ArchetypeRepository archetypeRepository;
    private final RestTemplate restTemplate;

    private String urlTest = "https://db.ygoprodeck.com/api/v7/cardinfo.php?name=";


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

    @Override
    @Transactional
    public String addCard(String cardName){
        CardResponse cardResponse = findByName(cardName);
        if(cardResponse.getName() != null){
            return cardResponse.getName();
        }
        try {
            String response = restTemplate.getForObject(urlTest + cardName, String.class);
            if (response== null){
                return "Card not found";
            }
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            JsonArray dataArray = jsonObject.getAsJsonArray("data");
            if (dataArray != null && !dataArray.isEmpty()) {
                JsonObject cardObject = dataArray.get(0).getAsJsonObject();
                String id = cardObject.get("id").getAsString();
                String name = cardObject.get("name").getAsString();
                String desc = cardObject.get("desc").getAsString();
                String type = cardObject.get("type").getAsString();
                String archetype = cardObject.has("archetype") ? cardObject.get("archetype").getAsString() : "N/A";

                String imageUrl = "";
                if (cardObject.has("card_images")) {
                    JsonArray cardImagesArray = cardObject.getAsJsonArray("card_images");
                    if (!cardImagesArray.isEmpty()) {
                        JsonObject firstImage = cardImagesArray.get(0).getAsJsonObject();
                        imageUrl = firstImage.get("image_url").getAsString();
                    }
                }

                CartaRequest card = CartaRequest.builder()
                        .code(id)
                        .name(name)
                        .description(desc)
                        .type(type)
                        .image(imageUrl)
                        .archetype(archetype).build();
                save(card);
                return card.getName();
            }
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "Card not found";
    }

    @Override
    @Transactional
    public String removeArchetype(CartaArchetype cartaArchetype) {
        Optional<Carta> carta = cartaRepository.findByName(cartaArchetype.getName());
        Optional<Archetype> archetype = archetypeRepository.findByName(cartaArchetype.getArchetype());
        if(carta.isPresent() && archetype.isPresent()){
            List<Archetype> archetypeList = carta.get().getArchetypes();
            if(archetypeList != null && archetypeList.contains(archetype.get()) ){
                archetypeList.remove(archetype.get());
                carta.get().setArchetypes(archetypeList);
                cartaRepository.save(carta.get());
                return "Archetype " + archetype.get().getName() + " was removed from " + carta.get().getName();
            }
            return "The card not contains this archetype";

        }
        return "Card or archetype not found";
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
        return CardResponse.builder().name("notFound").build();
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
