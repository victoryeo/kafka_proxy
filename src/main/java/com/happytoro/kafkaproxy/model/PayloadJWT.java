package com.happytoro.kafkaproxy.model;

import lombok.Data;

@Data
public class PayloadJWT {
    private String email;
    private String iat;
    private String exp;
}
