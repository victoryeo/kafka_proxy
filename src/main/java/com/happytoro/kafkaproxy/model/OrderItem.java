package com.happytoro.kafkaproxy.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize
public class OrderItem {
  private long orderID; 
  private String tokenType;
  private String tokenName;
  private int orderType;
  private float price;
  private float quantity;

  public OrderItem(
    long orderID, 
    String tokenType,
    String tokenName,
    int orderType,
    float price,
    float quantity) {
      this.orderID = orderID;
      this.tokenType = tokenType;
      this.tokenName = tokenName;
      this.orderType = orderType;
      this.price = price;
      this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "Order id=" + orderID + " " + tokenType + " " + tokenName +
        " " + orderType + " " + price + " " + quantity;
  }
}
