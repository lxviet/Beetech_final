package com.beetech.springsecurity.domain.service;

import com.beetech.springsecurity.domain.entity.City;
import com.beetech.springsecurity.domain.repository.CityRepository;
import com.beetech.springsecurity.web.dto.response.City.CityDto;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    public List<CityDto> getAllCities() {
        List<City> citiess = Lists.newArrayList(cityRepository.findAll());
        return citiess.stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());

    }

    public CityDto getCityById(int cityId){
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found with id: " + cityId));
        return modelMapper.map(city, CityDto.class);
    }
}
