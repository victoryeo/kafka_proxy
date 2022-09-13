package com.happytoro.kafkaproxy.model;

import lombok.Data;

@Data
public class Order {
  private int orderType;
  private int price;
  private int quantity;
}