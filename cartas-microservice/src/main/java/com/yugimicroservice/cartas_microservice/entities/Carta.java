package com.yugimicroservice.cartas_microservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cards")

@Getter @Setter
@Builder
public class Carta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String description;
    private String image;
    private String type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "carta_archetype",
            joinColumns = @JoinColumn(name = "carta_id"),
            inverseJoinColumns = @JoinColumn(name="archetype_id")
    )
    private List<Archetype> archetypes;

    public Carta() {
        this.archetypes = new ArrayList<Archetype>();
    }

    public Carta(Long id, String code, String name, String description, String image, String type, List<Archetype> archetypes) {
        super();
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.image = image;
        this.type = type;
        this.archetypes = archetypes;
    }
}
