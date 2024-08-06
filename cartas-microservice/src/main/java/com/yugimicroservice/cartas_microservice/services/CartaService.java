package com.yugimicroservice.cartas_microservice.services;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.entities.Carta;
import com.yugimicroservice.cartas_microservice.entities.dto.CardFoundResponse;
import com.yugimicroservice.cartas_microservice.entities.dto.CardResponse;
import com.yugimicroservice.cartas_microservice.entities.dto.CartaArchetype;
import com.yugimicroservice.cartas_microservice.entities.dto.CartaRequest;

import java.util.List;

public interface CartaService{
     List<CardResponse> findAll();
     CardResponse findByName(String name);
     CardResponse findByCode(String code);
     List<CardResponse> findByArchetype(String name);
     CardFoundResponse cardFound(String name, String code);
     void save(CartaRequest carta);
     String addArchetype(CartaArchetype cartaArchetype);
}
