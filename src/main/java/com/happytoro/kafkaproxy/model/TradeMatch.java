package com.happytoro.kafkaproxy.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize
public class TradeMatch {
    private Integer userId;
    private Integer tradeID;
    private String takerOrderId;
    private String makerOrderId;
    private String tokenType;
    private String tokenName;
    private Float price;

    private Float quantity;
    private String timestamp;

    public TradeMatch(
            Integer userId,
            Integer tradeID,
            String takerOrderId,
            String makerOrderId,
            String tokenType,
            String tokenName,
            Float price,
            Float quantity,
            String timestamp) {
        this.userId = userId;
        this.tradeID = tradeID;
        this.takerOrderId = takerOrderId;
        this.makerOrderId = makerOrderId;
        this.tokenType = tokenType;
        this.tokenName = tokenName;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }
}
