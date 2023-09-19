package com.beetech.springsecurity.web.dto.response.District;

import com.beetech.springsecurity.web.dto.response.City.CityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDto {
    private int id;
    private String name;
    private CityDto cityDto;
}
