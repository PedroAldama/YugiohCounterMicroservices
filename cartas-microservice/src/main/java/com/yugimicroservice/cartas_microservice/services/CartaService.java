package com.yugimicroservice.cartas_microservice.services;

import com.yugimicroservice.cartas_microservice.entities.Archetype;
import com.yugimicroservice.cartas_microservice.entities.Carta;
import com.yugimicroservice.cartas_microservice.entities.dto.CartaRequest;

import java.util.List;

public interface CartaService{
     List<Carta> findAll();
     Carta findById(Long id);
     Carta findByName(String name);
     Carta findByCode(String code);
     List<Carta> findByArchetype(String name);

     void save(CartaRequest carta);
}
