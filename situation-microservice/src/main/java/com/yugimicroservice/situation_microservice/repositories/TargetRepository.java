package com.yugimicroservice.situation_microservice.repositories;

import com.yugimicroservice.situation_microservice.entities.TargetCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TargetRepository extends JpaRepository<TargetCard, Long> {

}
