package com.happytoro.kafkaproxy.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.happytoro.kafkaproxy.model.Price;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {
  @Query("SELECT t FROM Price t WHERE t.tokenName = ?1")
  List<Price> findByNameEquals(String name);
}
