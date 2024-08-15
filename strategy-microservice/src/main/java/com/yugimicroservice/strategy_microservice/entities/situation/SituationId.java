package com.yugimicroservice.strategy_microservice.entities.situation;

import com.yugimicroservice.strategy_microservice.entities.Strategy;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SituationId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "situation_id")
    private Long situationId;

    @ManyToMany(mappedBy = "situations")
    List<Strategy> strategies;
}
