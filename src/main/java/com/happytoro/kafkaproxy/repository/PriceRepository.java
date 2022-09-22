package com.happytoro.kafkaproxy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.happytoro.kafkaproxy.model.Price;

import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {
  
}
