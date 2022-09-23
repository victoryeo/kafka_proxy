package com.happytoro.kafkaproxy.database;

// Importing required classes
import java.util.List;
import com.happytoro.kafkaproxy.model.Price;

// Interface
public interface PriceService {
  
    // Save operation
    Price savePrice(Price p);
  
    // Read operation
    List<Price> fetchPriceList();
  
    // Get operation
    Price getPriceById(Integer id);

    // Get operation
    List<Price> getPriceByTokenName(String name);

    // Delete operation
    void deletePriceById(Integer id);
}