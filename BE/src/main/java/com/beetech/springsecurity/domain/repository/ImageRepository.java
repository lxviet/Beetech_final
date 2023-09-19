package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.Image;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface ImageRepository extends ListPagingAndSortingRepository<Image, Integer>, ListCrudRepository<Image, Integer> {
}
