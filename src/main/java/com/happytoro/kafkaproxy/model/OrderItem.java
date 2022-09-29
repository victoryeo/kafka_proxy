package com.happytoro.kafkaproxy.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize
public class OrderItem {
  private String orderID; 
  private String tokenType;
  private String tokenName;
  private int orderType;
  private Double price;
  private Double quantity;

  public OrderItem(
    String orderID, 
    String tokenType,
    String tokenName,
    int orderType,
    Double price,
    Double quantity) {
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
