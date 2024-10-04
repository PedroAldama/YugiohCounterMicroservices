package com.yugimicroservice.strategy_microservice.controllers;

import com.yugimicroservice.strategy_microservice.entities.dto.StrategyAddRequest;
import com.yugimicroservice.strategy_microservice.entities.dto.StrategyRequest;
import com.yugimicroservice.strategy_microservice.entities.dto.StrategyResponse;
import com.yugimicroservice.strategy_microservice.services.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/strategy")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StrategyControllers {

    private final StrategyService strategyService;

    @GetMapping("/list")
    public List<StrategyResponse> getAll() {
        return strategyService.getAllStrategies();
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody StrategyRequest strategyRequest) {
        StrategyResponse strategyResponse = strategyService.save(strategyRequest);
        if(strategyResponse == null) {
            return ResponseEntity.ok("No se pudo crear");
        }
        return  ResponseEntity.ok(strategyResponse.toString());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<StrategyResponse> getByName(@PathVariable String name) {
        StrategyResponse strategyResponse = strategyService.getStrategyByName(name);
        if (strategyResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(strategyResponse);

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<StrategyResponse> getById(@PathVariable Long id) {
        StrategyResponse strategyResponse = strategyService.getStrategyById(id);
        if (strategyResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(strategyResponse);
    }

    @PostMapping("/add/situation")
    public ResponseEntity<List<String>> addSituation(@RequestBody StrategyAddRequest strategyRequest) {
        List<String> stringList = strategyService.addSituation(strategyRequest);
        if(stringList.isEmpty()){
            return ResponseEntity.ok(List.of("Strategy or Situations not found"));
        }
        return ResponseEntity.ok(stringList);
    }

    @PostMapping("/add/archetype")
    public ResponseEntity<List<String>> addArchetype(@RequestBody StrategyAddRequest strategyRequest) {
        List<String> stringList = strategyService.addArchetype(strategyRequest);
        if(stringList.isEmpty()){
            return ResponseEntity.ok(List.of("Strategy or Archetypes not found"));
        }
        return ResponseEntity.ok(stringList);
    }

    @DeleteMapping("/remove/archetype")
    public ResponseEntity<String> removeArchetype(@RequestBody StrategyAddRequest strategyRequest) {
        return ResponseEntity.ok(strategyService.removeArchetype(strategyRequest));

    }@DeleteMapping("/remove/situation")
    public ResponseEntity<String> removeSituation(@RequestBody StrategyAddRequest strategyRequest) {
        return ResponseEntity.ok(strategyService.removeSituation(strategyRequest));
    }
}
