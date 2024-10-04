package com.yugimicroservice.cartas_microservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yugimicroservice.cartas_microservice.services.ArchetypeService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter @Setter
@Builder
@JsonIgnoreProperties("cards")
public class Archetype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    @ManyToMany(mappedBy = "archetypes")
    private List<Carta> cards;

    public Archetype() {
        this.cards = new ArrayList<>();
    }

    public Archetype(Long id, String name, List<Carta> cards) {
        super();
        this.id = id;
        this.name = name;
        this.cards = cards;
    }
}
