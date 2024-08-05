package com.yugimicroservice.situation_microservice.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "target_card")
@NoArgsConstructor @AllArgsConstructor
@Setter @Getter
@Builder
public class TargetCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "card_code")
    private String cardCode;
    private String cardName;
}
