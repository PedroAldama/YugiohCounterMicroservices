package com.yugimicroservice.situation_microservice.servicies;

import com.yugimicroservice.situation_microservice.entities.CounterCard;
import com.yugimicroservice.situation_microservice.entities.Situation;
import com.yugimicroservice.situation_microservice.entities.TargetCard;
import com.yugimicroservice.situation_microservice.entities.dto.*;
import com.yugimicroservice.situation_microservice.repositories.CounterRepository;
import com.yugimicroservice.situation_microservice.repositories.SituationRepository;
import com.yugimicroservice.situation_microservice.repositories.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SituationServiceImpl implements SituationService {

    private final SituationRepository situationRepository;
    private final TargetRepository targetRepository;
    private final CounterRepository counterRepository;
    private final RestTemplate restTemplate;

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    @Override
    public void save(Situation situation) {
        situationRepository.save(situation);
    }

    @Override
    public List<SituationResponse> getAllSituationsByArchetype(String archetype) {
        return situationRepository.findAllByArchetype(archetype).stream().map(this::toResponse).toList();
    }

    @Override
    public String createSituation(SituationRequest situationRequest) {
        ArchetypeBoolean response = restTemplate.getForObject(
                basePath+"archetype/findIfExist/"+situationRequest.getArchetype()
                , ArchetypeBoolean.class);

        Boolean data = response.getFound();
        if (!data) {
            return "Situation could not be created, Archetype not found";
        }

        Situation situation = Situation.builder()
                .description(situationRequest.getDescription())
                .name(situationRequest.getName())
                .archetype(situationRequest.getArchetype())
                .build();
        save(situation);
        return "Situation was be created for " + situation.getDescription();

    }

    @Override
    public void addTargetCard(String name, CartaRequest target) {
        Optional<Situation> optionalSituation = situationRepository.findByName(name);
        if(optionalSituation.isEmpty()){
            return;
        }
        Situation situation = optionalSituation.orElseThrow();
        List<TargetCard> cardList = new ArrayList<>();
        if(!situation.getTargetCard().isEmpty()){
            cardList = situation.getTargetCard();
        }
        TargetCard targetCard = TargetCard.builder()
                .cardName(target.getCardName()).cardCode(target.getCardCode()).build();
        targetRepository.save(targetCard);
        cardList.add(targetCard);
        situation.setTargetCard(cardList);
        save(situation);
    }

    @Override
    public void addCounterCard(String name, CartaRequest counter) {
        Optional<Situation> optionalSituation = situationRepository.findByName(name);
        if(optionalSituation.isEmpty()){
            return;
        }
        Situation situation = optionalSituation.orElseThrow();
        List<CounterCard> cardList = new ArrayList<>();
        if(!situation.getCounterCard().isEmpty()){
            cardList = situation.getCounterCard();
        }
        CounterCard counterCard = CounterCard.builder()
                .cardName(counter.getCardName()).cardCode(counter.getCardCode()).build();
        counterRepository.save(counterCard);
        cardList.add(counterCard);
        situation.setCounterCard(cardList);
        save(situation);
    }

    @Override
    public ArchetypeFound getArchetype(String name) {
        System.out.println(basePath);
        return restTemplate.getForObject(
                basePath+"archetype/"+name
                , ArchetypeFound.class);
    }

    @Override
    public List<SituationResponse> getSituationForCard(String card) {
        Optional<TargetCard> optionalTargetCard = targetRepository.findByCardName(card);
        return optionalTargetCard.map(targetCard -> situationRepository.findByTargetCard(targetCard)
                .stream().map(newCard -> SituationResponse.builder()
                .name(newCard.getName())
                .description(newCard.getDescription())
                .archetype(newCard.getArchetype())
                .targetCards(TargetToCardResponse(newCard.getTargetCard()))
                .counterCards(CounterToCardResponse(newCard.getCounterCard()))
                .build()).toList()).orElseGet(ArrayList::new);
    }

    private List<CardResponse> TargetToCardResponse(List<TargetCard> situation) {
        return situation.stream().map(card->{
           CardResponse cardResponse = restTemplate
                   .getForObject(basePath+card.getCardName(),CardResponse.class);
            if(cardResponse.getCode().isEmpty()){
                return CardResponse.builder().build();
            }
          return CardResponse.builder().code(cardResponse.getCode())
                   .name(cardResponse.getName())
                   .description(cardResponse.getDescription())
                   .image(cardResponse.getImage())
                   .type(cardResponse.getType())
                   .build();
        }).toList();

    }
    private List<CardResponse> CounterToCardResponse(List<CounterCard> counter) {
        return counter.stream().map(card->{
           CardResponse cardResponse = restTemplate
                   .getForObject(basePath+card.getCardName(),CardResponse.class);

           if(cardResponse.getCode().isEmpty()){
               return CardResponse.builder().build();
           }
          return CardResponse.builder()
                   .code(cardResponse.getCode())
                   .name(cardResponse.getName())
                   .description(cardResponse.getDescription())
                   .image(cardResponse.getImage())
                   .type(cardResponse.getType())
                   .build();
        }).toList();
    }
    private SituationResponse toResponse(Situation situation){
        return SituationResponse.builder()
                .name(situation.getName())
                .description(situation.getDescription())
                .archetype(situation.getArchetype())
                .targetCards(TargetToCardResponse(situation.getTargetCard()))
                .counterCards(CounterToCardResponse(situation.getCounterCard()))
                .build();
    }
}
