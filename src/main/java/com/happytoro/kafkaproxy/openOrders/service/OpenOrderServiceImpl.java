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

    @Override
    public void updateOpenOrder(Long makerOrderID, Long takerOrderID, float amount) {
        // update maker order id
        OpenOrder makerOpenOrder = openOrderRepository.findByOrderId(makerOrderID);

        // check if order will be fulfilled, delete if yes.
        if (makerOpenOrder.getOpenAmount() + amount == makerOpenOrder.getInitialAmount()) {
            this.deleteOpenOrder(makerOrderID);
        }
        else {
            makerOpenOrder.setOpenAmount(makerOpenOrder.getOpenAmount() + amount);
            openOrderRepository.save(makerOpenOrder);
        }

        // update taker order id
        OpenOrder takerOpenOrder = openOrderRepository.findByOrderId(takerOrderID);

        // check if order will be fulfilled, delete if yes.
        if (takerOpenOrder.getOpenAmount() + amount == takerOpenOrder.getInitialAmount()) {
            this.deleteOpenOrder(makerOrderID);
        }
        else {
            takerOpenOrder.setOpenAmount(takerOpenOrder.getOpenAmount() + amount);
            openOrderRepository.save(takerOpenOrder);
        }


    }

    public void deleteOpenOrder(Long orderID) {
        OpenOrder openOrder = openOrderRepository.findByOrderId(orderID);
        openOrderRepository.delete(openOrder);
    }


}
