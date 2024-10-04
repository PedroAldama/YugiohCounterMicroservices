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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
    public SituationResponse getById(Long id) {
        Optional<Situation> optionalSituation = situationRepository.findById(id);
        if(optionalSituation.isEmpty()) {
            return  SituationResponse.builder().build();
        }
        return toResponse(optionalSituation.get());
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
    public String addTargetCard(String name, RequestCardName cardName) {
        Optional<Situation> optionalSituation = situationRepository.findByName(name);
        boolean contains = false;

        if(optionalSituation.isEmpty()){
            return "Situation not found";
        }
        Situation situation = optionalSituation.orElseThrow();

        List<TargetCard> cardList = new ArrayList<>();
        if(!situation.getTargetCard().isEmpty()){
            cardList = situation.getTargetCard();
            contains = cardList.stream().anyMatch(value -> value.getCardName().equals(cardName.getCardName()));
        }
        if(contains){
            return "This situation already includes this card";
        }
        CardResponse response = card(cardName);
        if(response.getName() != null){
            TargetCard targetCard = TargetCard.builder()
                    .cardName(Objects.requireNonNull(response).getName()).cardCode(Objects.requireNonNull(response).getCode()).build();
            targetRepository.save(targetCard);
            cardList.add(targetCard);
        }

        situation.setTargetCard(cardList);
        save(situation);
        return "Target card was added";
    }

    @Override
    public String addCounterCard(String name, RequestCardName cardName) {
        Optional<Situation> optionalSituation = situationRepository.findByName(name);
        boolean contains = false;
        if(optionalSituation.isEmpty()){
            return "Situation not found";
        }
        Situation situation = optionalSituation.orElseThrow();
        List<CounterCard> cardList = new ArrayList<>();
        if(!situation.getCounterCard().isEmpty()){
            cardList = situation.getCounterCard();
            contains = cardList.stream().anyMatch(value -> value.getCardName().equals(cardName.getCardName()));
        }
        if(contains){
            return "This situation  already includes this card";
        }
        CardResponse response = card(cardName);
        if(response.getName() != null){
            CounterCard counterCard = CounterCard.builder()
                    .cardName(Objects.requireNonNull(response).getName()).cardCode(Objects.requireNonNull(response).getCode()).build();
            counterRepository.save(counterCard);
            cardList.add(counterCard);
        }

        situation.setCounterCard(cardList);
        save(situation);
        return "Counter card was added";
    }

    private CardResponse card(RequestCardName cardName) {
        CardResponse response = restTemplate.getForObject(
                basePath+cardName.getCardName()
                , CardResponse.class);

        if(response.getName() == null){
            ResponseEntity<String> responsePost = searchForCard(cardName);
            if(Objects.equals(responsePost.getBody(), cardName.getCardName())){
                response = restTemplate.getForObject(
                        basePath + cardName.getCardName()
                        , CardResponse.class);
            }
        }
        return response;
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
                .stream().map(newCard -> SituationResponse.builder().id(newCard.getId())
                .name(newCard.getName())
                .description(newCard.getDescription())
                .archetype(newCard.getArchetype())
                .targetCards(TargetToCardResponse(newCard.getTargetCard()))
                .counterCards(CounterToCardResponse(newCard.getCounterCard()))
                .build()).toList()).orElseGet(ArrayList::new);
    }

    @Override
    public SituationFound getIfExists(Long id) {
        Optional<Situation> optionalSituation = situationRepository.findById(id);
       return SituationFound.builder().found(optionalSituation.isPresent()).build();
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
                .id(situation.getId())
                .name(situation.getName())
                .description(situation.getDescription())
                .archetype(situation.getArchetype())
                .targetCards(TargetToCardResponse(situation.getTargetCard()))
                .counterCards(CounterToCardResponse(situation.getCounterCard()))
                .build();
    }
    private ResponseEntity<String> searchForCard(RequestCardName cardName){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(cardName.getCardName(), headers);

            return restTemplate.exchange(
                    basePath+"create",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
    }
}
