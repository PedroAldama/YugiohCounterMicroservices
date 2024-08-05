package com.yugimicroservice.situation_microservice.controllers;

import com.yugimicroservice.situation_microservice.entities.Situation;
import com.yugimicroservice.situation_microservice.entities.dto.ArchetypeFound;
import com.yugimicroservice.situation_microservice.entities.dto.CartaRequest;
import com.yugimicroservice.situation_microservice.entities.dto.SituationRequest;
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

    @GetMapping("/archetype/{name}")
    public List<Situation> getByArchetype(@PathVariable String name) {
        return situationService.getAllSituationsByArchetype(name);
    }

    @PostMapping
    public ResponseEntity<?> createSituation(@RequestBody SituationRequest situation) {
        return ResponseEntity.ok().body(situationService.createSituation(situation));
    }

    @GetMapping("/{name}")
    public ArchetypeFound getArchetype(@PathVariable String name){
        return situationService.getArchetype(name);
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

}
