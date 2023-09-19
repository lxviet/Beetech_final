package com.beetech.springsecurity.web.controller;

import com.beetech.springsecurity.domain.entity.City;
import com.beetech.springsecurity.domain.service.CityService;
import com.beetech.springsecurity.web.dto.response.Category.CategoryDto;
import com.beetech.springsecurity.web.dto.response.City.CityDto;
import com.beetech.springsecurity.web.dto.response.User.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/city")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CityController {
    private final CityService cityService;
    @GetMapping("/getAllCity")
    public ResponseEntity<List<CityDto>> getAllCity(){
        List<CityDto> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable("id") int cityId){
        CityDto city = cityService.getCityById(cityId);
        return ResponseEntity.ok(city);
    }
}
