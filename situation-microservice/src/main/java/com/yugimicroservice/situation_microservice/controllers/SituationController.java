package com.yugimicroservice.situation_microservice.controllers;

import com.yugimicroservice.situation_microservice.entities.Situation;
import com.yugimicroservice.situation_microservice.entities.dto.*;
import com.yugimicroservice.situation_microservice.servicies.SituationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/situation")
@RequiredArgsConstructor
public class SituationController {
    private final SituationService situationService;

    @GetMapping("/list-archetype/{name}")
    public List<SituationResponse> getByArchetype(@PathVariable String name) {
        return situationService.getAllSituationsByArchetype(name);
    }

    @GetMapping("/id/{id}")
    public SituationResponse getById(@PathVariable Long id) {
        return situationService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> createSituation(@RequestBody SituationRequest situation) {
        return ResponseEntity.ok().body(situationService.createSituation(situation));
    }

   @GetMapping("/archetype/{name}")
    public ArchetypeFound getArchetype(@PathVariable String name){
        return situationService.getArchetype(name);
    }

    @GetMapping("/{name}")
    public List<SituationResponse> getSituation(@PathVariable String name) {
        return situationService.getSituationForCard(name);
    }

    @PostMapping("/{name}/addTarget")
    public ResponseEntity<?> addTarget(@PathVariable String name, @RequestBody CartaRequest cartaRequest) {
        situationService.addTargetCard(name,cartaRequest);
        return ResponseEntity.ok().body("Successful add card");
    }

    @PostMapping("/{name}/addCounter")
    public ResponseEntity<?> addCounter(@PathVariable String name, @RequestBody CartaRequest cartaRequest ){
        situationService.addCounterCard(name,cartaRequest);
        return ResponseEntity.ok().body("Successful add card");
    }

    @GetMapping("/exist/{id}")
    public SituationFound getIfExist(@PathVariable Long id){
        return situationService.getIfExists(id);
    }


}
