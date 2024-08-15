package com.yugimicroservice.situation_microservice.entities.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SituationFound {
    private Boolean found;
}
