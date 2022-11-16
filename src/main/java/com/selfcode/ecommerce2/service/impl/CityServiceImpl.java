package com.selfcode.ecommerce2.service.impl;


import com.selfcode.ecommerce2.model.City;
import com.selfcode.ecommerce2.repository.CityRepository;
import com.selfcode.ecommerce2.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
  @Autowired
  private CityRepository cityRepository;

  @Override
  public List<City> getAll() {
    return cityRepository.findAll();
  }
}
