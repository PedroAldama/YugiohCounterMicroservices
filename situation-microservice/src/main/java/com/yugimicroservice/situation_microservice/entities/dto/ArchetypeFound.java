package com.yugimicroservice.situation_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArchetypeFound {
    private Long id;
    private String name;
}
