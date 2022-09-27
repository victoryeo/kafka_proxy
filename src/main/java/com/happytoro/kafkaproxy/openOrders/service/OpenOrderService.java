package com.happytoro.kafkaproxy.openOrders.service;

import com.happytoro.kafkaproxy.openOrders.model.OpenOrder;

public interface OpenOrderService {
    OpenOrder saveOpenOrder(OpenOrder order);
}
