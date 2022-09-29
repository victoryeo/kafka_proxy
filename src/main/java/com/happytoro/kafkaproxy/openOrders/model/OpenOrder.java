package com.happytoro.kafkaproxy.openOrders.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "openOrders")
public class OpenOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(
      name = "native",
      strategy = "native"
    )
    private Integer id;

    private String orderId;
    private String tokenType;
    private String tokenName;
    private Double initialAmount;
    private Double openAmount;
    private Integer orderType;
    // should attach array of fcm to userId in v2, so all devices will be notified.
    // private String userId; 

    public OpenOrder() {}

    public OpenOrder(String orderId, String tokenType, String tokenName, Double initialAmount, Double openAmount, Integer orderType) {
        this.orderId = orderId;
        this.tokenType = tokenType;
        this.tokenName = tokenName;
        this.initialAmount = initialAmount;
        this.openAmount = openAmount;
        this.orderType = orderType;
    }
}
