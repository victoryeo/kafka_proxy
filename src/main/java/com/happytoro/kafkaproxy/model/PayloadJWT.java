package com.happytoro.kafkaproxy.model;

import lombok.Data;

@Data
public class PayloadJWT {
    private String email;
    private String iat; // issued at
    private String exp; // expiry time
}
