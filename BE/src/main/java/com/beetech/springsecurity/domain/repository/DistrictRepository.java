package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.City;
import com.beetech.springsecurity.domain.entity.District;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface DistrictRepository extends ListPagingAndSortingRepository<District, Integer>, ListCrudRepository<District, Integer> {
    District findByName(String name);

}


