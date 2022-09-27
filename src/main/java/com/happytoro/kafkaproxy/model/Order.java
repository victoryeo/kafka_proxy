package com.happytoro.kafkaproxy.model;

import lombok.Data;

@Data
public class Order {
  private String orderID; 
  private String tokenType;
  private String tokenName;
  private int orderType;
  private float price;
  private float quantity;
}