package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.CartDetail;

import java.util.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface CartDetailRepository extends ListPagingAndSortingRepository<CartDetail, Integer>, ListCrudRepository<CartDetail, Integer> {
    List<CartDetail> findAllByCart_id(int cartId);
    @Query("Select COALESCE(SUM(cd.quantity),0) " +
            "from CartDetail cd where cd.cart.id = :cartId")
    int sumQuantityByCardId(int cartId);


    @Query("Select COALESCE(SUM(cd.total_price),0) " +
            "from CartDetail cd where cd.cart.id = :cartId")
    long sumTotalPriceByCartId(int cartId);

}

