package com.beetech.springsecurity.domain.service;

import com.beetech.springsecurity.domain.entity.City;
import com.beetech.springsecurity.domain.entity.District;
import com.beetech.springsecurity.domain.repository.CityRepository;
import com.beetech.springsecurity.domain.repository.DistrictRepository;
import com.beetech.springsecurity.web.dto.response.City.CityDto;
import com.beetech.springsecurity.web.dto.response.District.DistrictDto;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;

    public List<DistrictDto> getAllDistricts() {
        List<District> districts = Lists.newArrayList(districtRepository.findAll());
        return districts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DistrictDto getDistrictById(int districtId) {
        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new IllegalArgumentException("District not found with id: " + districtId));
        DistrictDto districtDto = modelMapper.map(district, DistrictDto.class);
        CityDto cityDto = modelMapper.map(district.getCity(), CityDto.class);
        districtDto.setCityDto(cityDto);
        return districtDto;
    }
    private DistrictDto convertToDto(District district) {
        DistrictDto districtDto = modelMapper.map(district, DistrictDto.class);
        CityDto cityDto = modelMapper.map(district.getCity(), CityDto.class);
        districtDto.setCityDto(cityDto);
        return districtDto;
    }
}
