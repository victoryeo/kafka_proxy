package com.happytoro.kafkaproxy.openOrders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happytoro.kafkaproxy.openOrders.model.OpenOrder;
import com.happytoro.kafkaproxy.openOrders.repository.OpenOrderRepository;

@Service
public class OpenOrderServiceImpl implements OpenOrderService{
    
    @Autowired
    private OpenOrderRepository openOrderRepository;

    @Override
    public OpenOrder saveOpenOrder(OpenOrder order)
    {
      return openOrderRepository.save(order);
    }


}
