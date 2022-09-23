package com.happytoro.kafkaproxy.database;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happytoro.kafkaproxy.repository.PriceRepository;
import com.happytoro.kafkaproxy.model.Price;

@Service
public class PriceServiceImpl
    implements PriceService {
  
    @Autowired
    private PriceRepository priceRepository;

    @Override
    public Price savePrice(Price p)
    {
      return priceRepository.save(p);
    }

    @Override
    public List<Price> fetchPriceList() {
      return (List<Price>)priceRepository.findAll();
    }

    public Price getPriceById(Integer id) {
      return priceRepository.findById(id).get();
    }

    // Get operation
    public List<Price> getPriceByTokenName(String name) {
      return priceRepository.findByNameEquals(name);
    }
  
    @Override
    public void deletePriceById(Integer id)
    {
      priceRepository.deleteById(id);
    }
  }