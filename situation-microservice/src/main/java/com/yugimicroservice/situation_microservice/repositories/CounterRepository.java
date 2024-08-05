package com.yugimicroservice.situation_microservice.repositories;

import com.yugimicroservice.situation_microservice.entities.CounterCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends JpaRepository<CounterCard,Long> {
}
