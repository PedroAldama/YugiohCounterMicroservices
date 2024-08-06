package com.yugimicroservice.cartas_microservice.controllers;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.entities.dto.ArchetypeFoundResponse;
import com.yugimicroservice.cartas_microservice.entities.dto.ArchetypeResponse;
import com.yugimicroservice.cartas_microservice.services.ArchetypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carta/archetype")
@AllArgsConstructor
public class ArchetypeController {
    private final ArchetypeService archetypeService;

    @GetMapping
    public List<Archetype> getAll() {
        return archetypeService.findAll();
    }

    @GetMapping("/{name}")
    public ArchetypeResponse get(@PathVariable String name) {
        return archetypeService.findByName(name);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Archetype archetype) {
        return ResponseEntity.ok(archetypeService.save(archetype));
    }
    @GetMapping("/findIfExist/{name}")
    public ArchetypeFoundResponse findIdExist(@PathVariable String name) {
        return archetypeService.findIfExistByName(name);
    }

}
