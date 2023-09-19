package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.CategoryImage;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface CategoryImageRepository extends ListPagingAndSortingRepository<CategoryImage, Integer>, ListCrudRepository<CategoryImage, Integer> {
    CategoryImage findByCategoryId(int categoryId);
}

