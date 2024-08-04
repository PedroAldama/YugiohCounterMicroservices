package com.yugimicroservice.cartas_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartaRequest {
    private String code;
    private String name;
    private String description;
    private String image;
    private String type;
    private String archetype;
}
