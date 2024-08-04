package com.yugimicroservice.cartas_microservice.controllers;

import com.yugimicroservice.cartas_microservice.entities.Carta;
import com.yugimicroservice.cartas_microservice.entities.dto.CartaRequest;
import com.yugimicroservice.cartas_microservice.repositories.CartaRepository;
import com.yugimicroservice.cartas_microservice.services.CartaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carta")
@AllArgsConstructor
public class CartaController {

    private final CartaService cartaService;

    @GetMapping
    public List<Carta> getAll() {
        return cartaService.findAll();
    }

    @GetMapping("/{name}")
    public Carta getCarta(@PathVariable String name) {
        return cartaService.findByName(name);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CartaRequest carta) {
        cartaService.save(carta);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/archetype/{name}")
    public ResponseEntity<?> findByArchetype(@PathVariable String name) {
        List<Carta> carta = cartaService.findByArchetype(name);
        return ResponseEntity.ok(carta);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(cartaService.findByCode(code));
    }
}
