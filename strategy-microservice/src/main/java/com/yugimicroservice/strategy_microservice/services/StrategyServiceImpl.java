package com.yugimicroservice.strategy_microservice.services;

import com.yugimicroservice.strategy_microservice.entities.Strategy;
import com.yugimicroservice.strategy_microservice.entities.carta.Archetype;
import com.yugimicroservice.strategy_microservice.entities.dto.*;
import com.yugimicroservice.strategy_microservice.entities.dto.request.ArchetypeFound;
import com.yugimicroservice.strategy_microservice.entities.situation.SituationId;
import com.yugimicroservice.strategy_microservice.repositories.ArchetypeRepository;
import com.yugimicroservice.strategy_microservice.repositories.SituationRepository;
import com.yugimicroservice.strategy_microservice.repositories.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StrategyServiceImpl implements StrategyService {

    private final StrategyRepository strategyRepository;
    private final RestTemplate strategyRestTemplate;
    private final ArchetypeRepository archetypeRepository;
    private final SituationRepository situationRepository;

    @Value("${spring.url.path.archetypes}")
    private String basePathArchetypes;
    @Value("${spring.url.path.situation.path}")
    private String basePathSituation;

    @Transactional
    @Override
    public StrategyResponse save(StrategyRequest strategy) {
        Strategy newStrategy = Strategy.builder()
                .name(strategy.getName())
                .description(strategy.getDescription())
                .gameplay(strategy.getGameplay())
                .archetypes(getArchetypeList(strategy.getArchetypesId()))
                .build();

        strategyRepository.save(newStrategy);
        return StrategyToResponse(newStrategy);
    }

    @Override
    public List<StrategyResponse> getAllStrategies() {
        List<Strategy> strategies = strategyRepository.findAll();
        return strategies.stream().map(this::StrategyToResponse).toList();
    }

    @Override
    public StrategyResponse getStrategyById(Long id) {
        Optional<Strategy> optionalStrategy = strategyRepository.findById(id);
        return optionalStrategy.map(this::StrategyToResponse).orElse(null);
    }

    @Override
    public StrategyResponse getStrategyByName(String name) {
        Optional<Strategy> optionalStrategy = strategyRepository.findByName(name);
        return optionalStrategy.map(this::StrategyToResponse).orElse(null);
    }

    @Override
    public List<StrategyResponse> getAllByArchetype(String name) {
        try {
            Archetype request = strategyRestTemplate.getForObject(basePathArchetypes
                    + "/" + name, Archetype.class);
            if (request != null ) {
                List<Strategy> strategiesList = strategyRepository.findAllByArchetypes(request);
                return strategiesList.stream().map(this::StrategyToResponse).toList();
            }
        }catch (Exception e){
            return null;
        }
        return new ArrayList<>();
    }

    @Transactional
    @Override
    public List<String> addSituation(StrategyAddRequest request) {
       List<String> response = new ArrayList<>();
       List<SituationId> situationList = new ArrayList<>();
       Optional<Strategy> optionalStrategy = strategyRepository.findByName(request.getStrategy());

        if (optionalStrategy.isEmpty()) {
            return new ArrayList<>();
        }

        Strategy strategyFound = optionalStrategy.get();
        strategyFound.getSituations().forEach(archetype->{
            request.getIdList().remove(archetype.getId());
        });
        try {
            response =  request.getIdList().stream().map(id->{
                Optional<SituationId> optionalSituation = situationRepository.findById(id);
                if(optionalSituation.isEmpty()) {
                    //search in situation
                    Object situationRequest = strategyRestTemplate.getForObject(basePathSituation
                            + "exist/" + id, Object.class);
                    if (situationRequest == null || situationRequest.toString().equals("{found=false}")) {
                        return "Situation not exist";
                    }
                    SituationId newSituation = SituationId.builder().situationId(id).build();
                    situationRepository.save(newSituation);
                    situationList.add(newSituation);
                    return newSituation.getSituationId() + "was added to the strategy " + strategyFound.getName()
                            + " and new situation was added to the Strategy data";
                }

                situationList.add(optionalSituation.orElse(null));
                return optionalSituation.get().getId() + " was added to the strategy " + strategyFound.getName();
            }).toList();

        }catch (Exception e){
            return response;
        }

        if (situationList.isEmpty()) {
            return new ArrayList<>();
        }
        strategyFound.setSituations(situationList);
        strategyRepository.save(strategyFound);
        return response;
    }

    @Transactional
    @Override
    public List<String> addArchetype(StrategyAddRequest request) {
        List<String> response = new ArrayList<>();
        Optional<Strategy> optionalStrategy = strategyRepository.findByName(request.getStrategy());
        List<Archetype> archetypeList = new ArrayList<>();
        if (optionalStrategy.isEmpty()) {
            return new ArrayList<>();
        }
        Strategy strategyFound = optionalStrategy.get();
        strategyFound.getArchetypes().forEach(archetype->{
            request.getIdList().remove(archetype.getId());
        });
        try {
            response =  request.getIdList().stream().map(id->{
                Optional<Archetype> archetypeOptional = archetypeRepository.findById(id);
                if(archetypeOptional.isEmpty()) {
                    //search in situation
                    ArchetypeFound archetypeResponse = strategyRestTemplate.getForObject(basePathArchetypes
                            + "findIfExist/id/" + id, ArchetypeFound.class);
                    if (archetypeResponse == null || !archetypeResponse.getFound()) {
                        return "Situation not exist";
                    }
                    ArchetypeRequest archetypeFound = strategyRestTemplate.getForObject(basePathArchetypes + "id/"
                            + id, ArchetypeRequest.class);
                    if(archetypeFound == null) {
                        return "Archetype not exist";
                    }
                    Archetype newArchetype = Archetype.builder()
                            .archetypeName(archetypeFound.getName())
                            .archetypeId(archetypeFound.getId())
                            .build();

                    archetypeRepository.save(newArchetype);
                    archetypeList.add(newArchetype);
                    return newArchetype.getArchetypeName() + "was added to the strategy " + strategyFound.getName()
                            + " and new situation was added to the Strategy data";
                }
                archetypeList.add(archetypeOptional.orElse(null));
                return archetypeOptional.get().getArchetypeName() + " was added to the strategy " + strategyFound.getName();
            }).toList();
        }catch (Exception e){
            return response;
        }
        if (archetypeList.isEmpty()) {
            return new ArrayList<>();
        }
        archetypeList.addAll(strategyFound.getArchetypes());
        strategyFound.setArchetypes(archetypeList);
        strategyRepository.save(strategyFound);
        return response;
    }

    @Override
    public String removeSituation(StrategyAddRequest request) {
        Optional<Strategy> optionalStrategy = strategyRepository.findByName(request.getStrategy());
        if (optionalStrategy.isEmpty()) {
            return "Strategy not exist";
        }
        Strategy strategy = optionalStrategy.get();
        List<SituationId> situationId = new ArrayList<>(strategy.getSituations());

        if(situationId.isEmpty()) {
            return "Empty situation list";
        }
        situationId.removeIf(situation -> request.getIdList().contains(situation.getId()));
        strategy.setSituations(situationId);
        strategyRepository.save(strategy);
        return "situations removed from strategy " + strategy.getName();

    }

    @Override
    public String removeArchetype(StrategyAddRequest request) {
        Optional<Strategy> optionalStrategy = strategyRepository.findByName(request.getStrategy());
        if (optionalStrategy.isEmpty()) {
            return "Strategy not exist";
        }
        Strategy strategy = optionalStrategy.get();
        List<Archetype> archetypeList = new ArrayList<>(strategy.getArchetypes());

        if (archetypeList.isEmpty()) {
            return "Empty Archetypes";
        }
        archetypeList.removeIf(archetype -> request.getIdList().contains(archetype.getArchetypeId()));
        strategy.setArchetypes(archetypeList);
        strategyRepository.save(strategy);
        return "Archetypes removed";
    }

    private StrategyResponse StrategyToResponse(Strategy strategy) {
        return StrategyResponse.builder()
                .id(strategy.getId())
                .name(strategy.getName())
                .description(strategy.getDescription())
                .gameplay(strategy.getGameplay())
                .archetypeList(strategy.getArchetypes().stream().map(archetype -> {
                    return ArchetypeResponse.builder().id(archetype.getArchetypeId()).name(archetype.getArchetypeName()).build();
                }).toList())
                .situationIdList(strategy.getSituations().stream().map(situationId -> {
                    return SituationResponse.builder().id(situationId.getSituationId()).build();
                }).toList())
                .build();
    }

    private List<Archetype> getArchetypeList(List<Long> strategiesId) {

            return strategiesId.stream().map(id->{
                try {
                    ArchetypeRequest request = strategyRestTemplate.getForObject(basePathArchetypes + "archetype/id/"
                            + id, ArchetypeRequest.class);

                    if(request != null) {
                    Archetype archetype = Archetype.builder()
                            .archetypeId(request.getId())
                            .archetypeName(request.getName())
                            .build();
                    archetypeRepository.save(archetype);
                    return archetype;
                    } else{
                        return Archetype.builder().build();
                    }
                }catch (Exception ignored){
                    return null;
                }
            }).toList();
    }


}
