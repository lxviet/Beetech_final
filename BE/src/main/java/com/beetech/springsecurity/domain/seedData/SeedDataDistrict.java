package com.beetech.springsecurity.domain.seedData;

import com.beetech.springsecurity.SpringSecurityApplication;
import com.beetech.springsecurity.domain.entity.City;
import com.beetech.springsecurity.domain.entity.District;
import com.beetech.springsecurity.domain.repository.CityRepository;
import com.beetech.springsecurity.domain.repository.DistrictRepository;
import com.beetech.springsecurity.web.dto.response.District.DistrictDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SeedDataDistrict {
    private static final Logger log = LoggerFactory.getLogger(SpringSecurityApplication.class);

    @Bean
    public CommandLineRunner seedDistrits(DistrictRepository districtRepository, CityRepository cityRepository) {
        return (args) -> {


            City haNoi = cityRepository.findById(1).orElse(null);
            City hoChiMinh = cityRepository.findById(2).orElse(null);
            City haiPhong = cityRepository.findById(3).orElse(null);
            City daNang = cityRepository.findById(4).orElse(null);
            City canTho = cityRepository.findById(5).orElse(null);
            City anGiang = cityRepository.findById(6).orElse(null);
            City vungTau = cityRepository.findById(7).orElse(null);
            List<District> districts = Arrays.asList(
                    new District(1, "Thanh Xuân - HN", haNoi),
                    new District(2, "Đống Đa - HN", haNoi),
                    new District(3, "QUận 1 - HCM", hoChiMinh),
                    new District(4, "QUận 2 - HCM", hoChiMinh),
                    new District(5, "Hải Phòng - HP", haiPhong),
                    new District(6, "Hải Phòng 2 - HP", haiPhong),
                    new District(7, "Đà Nẵng - ĐN", daNang),
                    new District(8, "Đà Nẵng 2 - ĐN", daNang),
                    new District(9, "Cần Thơ - CT", canTho),
                    new District(10, "Cần Thơ 2 - CT", canTho),
                    new District(11, "An Giang - AG", anGiang),
                    new District(12, "An Giang 2 - AG", anGiang),
                    new District(13, "Bà Rịa - Vũng Tàu - VT", vungTau),
                    new District(14, "Bà Rịa - Vũng Tàu 2 - VT", vungTau)
            );

            districtRepository.saveAll(districts);

            log.info("Cities found with findAll():");
            log.info("-------------------------------");
            for (District district : districtRepository.findAll()) {
                log.info(district.toString());
            }
            log.info("");
        };
    }
}





