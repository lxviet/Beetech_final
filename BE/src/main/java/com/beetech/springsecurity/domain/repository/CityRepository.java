package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.CategoryImage;
import com.beetech.springsecurity.domain.entity.City;
import com.beetech.springsecurity.domain.entity.Product;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface CityRepository extends ListPagingAndSortingRepository<City, Integer>, ListCrudRepository<City, Integer> {
    City findByName(String name);

}
