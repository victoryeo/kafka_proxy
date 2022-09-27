package com.happytoro.kafkaproxy.openOrders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happytoro.kafkaproxy.openOrders.model.OpenOrder;

@Repository
public interface OpenOrderRepository extends JpaRepository<OpenOrder, Integer>{
    OpenOrder findByOrderId(Long orderId);
}
