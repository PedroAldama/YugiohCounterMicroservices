package com.yugimicroservice.strategy_microservice.entities.carta;

import com.yugimicroservice.strategy_microservice.entities.Strategy;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Archetype {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "archetype_id")
    private Long archetypeId;

    @Column(name = "archetype_name")
    private String archetypeName;

    @ManyToMany(mappedBy = "archetypes")
    private List<Strategy> strategies;
}
