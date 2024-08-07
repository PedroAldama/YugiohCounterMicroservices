package com.yugimicroservice.situation_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardResponse {
    private String code;
    private String name;
    private String description;
    private String image;
    private String type;
}
