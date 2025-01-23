package com.yugimicroservice.cartas_microservice.controllers;

import com.yugimicroservice.cartas_microservice.entities.dto.CardResponse;
import com.yugimicroservice.cartas_microservice.entities.dto.CartaArchetype;
import com.yugimicroservice.cartas_microservice.services.CartaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carta")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CartaController {

    private final CartaService cartaService;

    @GetMapping
    public List<CardResponse> getAll() {
        return cartaService.findAll();
    }

    @GetMapping("/{name}")
    public CardResponse getCarta(@PathVariable String name) {
        return cartaService.findByName(name);
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody String name) {
        return ResponseEntity.ok(cartaService.addCard(name));
    }

    @GetMapping("/archetypeName/{name}")
    public ResponseEntity<List<CardResponse>> findByArchetype(@PathVariable String name) {
        List<CardResponse> carta = cartaService.findByArchetype(name);
        return ResponseEntity.ok(carta);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CardResponse> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(cartaService.findByCode(code));
    }

    @PostMapping("/setArchetype")
    public ResponseEntity<String> setArchetype(@RequestBody CartaArchetype request) {
        return ResponseEntity.ok(cartaService.addArchetype(request));
    }

    @DeleteMapping("/deleteArchetype")
    public ResponseEntity<String> removeArchetype(@RequestBody CartaArchetype request) {
        return ResponseEntity.ok(cartaService.removeArchetype(request));
    }


}
