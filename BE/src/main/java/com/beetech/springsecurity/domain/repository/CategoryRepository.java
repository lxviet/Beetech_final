package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.Category;
import com.beetech.springsecurity.domain.entity.CategoryImage;
import com.beetech.springsecurity.domain.entity.MstUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface CategoryRepository extends ListPagingAndSortingRepository<Category, Integer>, ListCrudRepository<Category, Integer> {
    Optional<Category> findById(int Id);

}
