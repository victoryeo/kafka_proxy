package com.happytoro.kafkaproxy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


@Entity  
@Data
public class Price {    
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
  @GenericGenerator(
    name = "native",
    strategy = "native"
  )
  private long id;

  private String tokenType;
  private String tokenName;
  private int price;
  private String timeOfTrade;

  public Price() {}

  public Price(String type, String name, int price, String time) {
      this.tokenType = type;
      this.tokenName = name;
      this.price = price;
      this.timeOfTrade = time;
  }
}