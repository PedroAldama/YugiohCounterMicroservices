package com.yugimicroservice.cartas_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArchetypeFoundResponse {
    private Boolean found;
}
