package com.yugimicroservice.cartas_microservice.controllers;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
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
    public Archetype get(@PathVariable String name) {
        return archetypeService.findByName(name);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Archetype archetype) {
        archetypeService.save(archetype);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/findIfExist/{name}")
    public Boolean findIdExist(@PathVariable String name) {
        Boolean found = archetypeService.findIfExistByName(name);
        System.out.println(found);
        return found;
    }
}
