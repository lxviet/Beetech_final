package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.Cart;
import com.beetech.springsecurity.domain.entity.Category;
import com.beetech.springsecurity.domain.entity.MstUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface CartRepository extends ListPagingAndSortingRepository<Cart, Integer>, ListCrudRepository<Cart, Integer> {
    Optional<Cart> findByUser_id(int userId);

    Optional<Cart> findByToken(String token);

}
