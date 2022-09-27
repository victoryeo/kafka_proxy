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
    private float initialAmount;
    private float openAmount;
    private Integer orderType;

    public OpenOrder() {}

    public OpenOrder(String orderId, String tokenType, String tokenName, float initialAmount, float openAmount, Integer orderType) {
        this.orderId = orderId;
        this.tokenType = tokenType;
        this.tokenName = tokenName;
        this.initialAmount = initialAmount;
        this.openAmount = openAmount;
        this.orderType = orderType;
    }
}
