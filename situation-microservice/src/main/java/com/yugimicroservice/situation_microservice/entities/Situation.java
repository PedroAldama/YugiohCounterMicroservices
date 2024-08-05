package com.yugimicroservice.situation_microservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "situation")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Situation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String archetype;
    //private boolean active;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "situation_target",
            joinColumns = @JoinColumn(name = "situation_id"),
            inverseJoinColumns = @JoinColumn(name = "target_id")
    )
    private List<TargetCard> targetCard;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "situation_counter",
            joinColumns = @JoinColumn(name = "situation_id"),
            inverseJoinColumns = @JoinColumn(name = "counter_id")
    )
    private List<CounterCard> counterCard;
}
