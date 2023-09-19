package com.beetech.springsecurity.domain.seedData;
import com.beetech.springsecurity.SpringSecurityApplication;
import com.beetech.springsecurity.domain.entity.City;
import com.beetech.springsecurity.domain.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Component
public class SeedDataCity {
        private static final Logger log = LoggerFactory.getLogger(SpringSecurityApplication.class);

        @Bean
        public CommandLineRunner seedCities(CityRepository cityRepository) {
            return (args) -> {
                List<City> cities = Arrays.asList(
                        new City(1, "Hà Nội"),
                        new City(2, "Hồ Chí Minh"),
                        new City(3, "Hải Phòng"),
                        new City(4, "Đà Nẵng"),
                        new City(5, "Cần Thơ"),
                        new City(6, "An Giang"),
                        new City(7, "Bà Rịa - Vũng Tàu")

                );

                cityRepository.saveAll(cities);

                log.info("Cities found with findAll():");
                log.info("-------------------------------");
                for (City city : cityRepository.findAll()) {
                    log.info(city.toString());
                }
                log.info("");
            };
        }
    }


