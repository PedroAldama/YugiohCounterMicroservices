package com.yugimicroservice.situation_microservice.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "counter_card")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CounterCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "card_code")
    private String cardCode;
    private String cardName;
}
