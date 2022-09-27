package com.happytoro.kafkaproxy.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.List;

import com.happytoro.kafkaproxy.price.model.Price;
import com.happytoro.kafkaproxy.price.service.PriceService;

@RestController
@RequestMapping("/price/")
public class PriceController {
  Logger logger = LogManager.getLogger(RestController.class);

  @Autowired
  private PriceService priceService;

  @GetMapping("/list")
  public List<Price> getPriceList() {
      logger.info("getPriceList");
      return priceService.fetchPriceList();
  }

  @GetMapping("/{id}")
  public Price getPriceById(@PathVariable("id") Integer id) {
      logger.info("getPriceById");
      return priceService.getPriceById(id);
  }

  @GetMapping("/token/{name}")
  public List<Price> getPriceByTokenName(@PathVariable("name") String name) {
      logger.info("getPriceByTokenName");
      return priceService.getPriceByTokenName(name);
  }
}
