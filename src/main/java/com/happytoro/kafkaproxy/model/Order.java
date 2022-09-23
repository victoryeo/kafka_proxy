package com.happytoro.kafkaproxy.model;

import lombok.Data;

@Data
public class Order {
  private String tokenType;
  private String tokenName;
  private int orderType;
  private int price;
  private float quantity;
}