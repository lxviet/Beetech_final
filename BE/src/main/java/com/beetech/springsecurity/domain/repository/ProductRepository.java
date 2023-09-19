package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.Category;
import com.beetech.springsecurity.domain.entity.MstUser;
import com.beetech.springsecurity.domain.entity.Product;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends ListPagingAndSortingRepository<Product, Integer>, ListCrudRepository<Product, Integer> {
    Optional<Product> findBySku(String sku);


}
