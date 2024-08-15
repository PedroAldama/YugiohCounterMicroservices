package com.yugimicroservice.strategy_microservice.entities;

import com.yugimicroservice.strategy_microservice.entities.carta.Archetype;
import com.yugimicroservice.strategy_microservice.entities.situation.SituationId;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "strategy")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String gameplay;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "strategy_archetype",
            joinColumns = @JoinColumn(name = "strategy_id"),
            inverseJoinColumns = @JoinColumn(name = "archetype_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"strategy_id" , "archetype_id"})}
    )
    private List<Archetype> archetypes;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "strategy_situation",
            joinColumns = @JoinColumn(name = "strategy_id"),
            inverseJoinColumns = @JoinColumn(name = "situation_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"strategy_id" , "situation_id"})}
    )
    private List<SituationId> situations;


}
